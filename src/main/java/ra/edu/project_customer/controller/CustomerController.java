package ra.edu.project_customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.dto.request.CustomerRQ;
import ra.edu.project_customer.dto.response.CustomerResponse;
import ra.edu.project_customer.entity.Customer;
import ra.edu.project_customer.entity.CustomerGroup;
import ra.edu.project_customer.entity.RoleEnum;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.mapper.CustomerMapper;
import ra.edu.project_customer.service.CustomerGroupService;
import ra.edu.project_customer.service.CustomerService;
import ra.edu.project_customer.service.UserService;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;
    private final CustomerGroupService groupService;

//    @PostMapping
//    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRQ request) {
//        User user = userService.findById(request.getUserId());
//        CustomerGroup group = groupService.findById(request.getGroupId());
//
//        Customer customer = customerService.create(CustomerMapper.toEntity(request, user, group));
//        return ResponseEntity.ok(CustomerMapper.toResponse(customer));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Integer id,
                                                           @Valid @RequestBody CustomerRQ request,
                                                           Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        Customer existingCustomer = customerService.findById(id);

        if (currentUser.hasRole(RoleEnum.CUSTOMER)) {
            if (!existingCustomer.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new AccessDeniedException("Bạn không có quyền cập nhật thông tin người khác");
            }
        }

        CustomerGroup group = request.getGroupId() != null
                ? groupService.findById(request.getGroupId())
                : null;

        existingCustomer.setGroup(group);
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setCity(request.getCity());
        existingCustomer.setCountry(request.getCountry());
//        existingCustomer.setStatus(request.getStatus());

        Customer updated = customerService.save(existingCustomer);
        return ResponseEntity.ok(CustomerMapper.toResponse(updated));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.findById(id);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(currentUsername);

        boolean isAdminOrStaff = currentUser.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getRoleName().name())
                .anyMatch(role -> role.equals("ADMIN") || role.equals("STAFF"));

        if (!isAdminOrStaff && !customer.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("Bạn không có quyền xem thông tin khách hàng này.");
        }

        return ResponseEntity.ok(CustomerMapper.toResponse(customer));
    }


    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(
            @RequestParam(required = false, defaultValue = "") String name,
            Pageable pageable) {
        Page<Customer> page = customerService.findAll(name, pageable);
        return ResponseEntity.ok(page.map(CustomerMapper::toResponse));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer id,
                                             @RequestParam String status) {
        customerService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerResponse> getByUserId(@PathVariable Integer userId) {
        Customer customer = customerService.findByUserId(userId);
        return ResponseEntity.ok(CustomerMapper.toResponse(customer));
    }
}
