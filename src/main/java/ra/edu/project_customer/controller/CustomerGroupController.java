package ra.edu.project_customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.dto.request.CustomerGroupRQ;
import ra.edu.project_customer.dto.response.CustomerGroupDTO;
import ra.edu.project_customer.entity.CustomerGroup;
import ra.edu.project_customer.mapper.CustomerGroupMapper;
import ra.edu.project_customer.service.CustomerGroupService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customer-groups")
@RequiredArgsConstructor
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    @GetMapping
    public ResponseEntity<List<CustomerGroupDTO>> getAllGroups() {
        List<CustomerGroup> groups = customerGroupService.getAllGroups();
        List<CustomerGroupDTO> result = groups.stream()
                .map(CustomerGroupMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<CustomerGroupDTO> createGroup(@Valid @RequestBody CustomerGroupRQ dto) {
        CustomerGroup entity = CustomerGroupMapper.toEntity(dto);
        CustomerGroup saved = customerGroupService.createGroup(entity);
        return ResponseEntity.ok(CustomerGroupMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerGroupDTO> updateGroup(@PathVariable Integer id,
                                                        @Valid @RequestBody CustomerGroupRQ dto) {
        CustomerGroup entity = CustomerGroupMapper.toEntity(dto);
        CustomerGroup updated = customerGroupService.updateGroup(id, entity);
        return ResponseEntity.ok(CustomerGroupMapper.toDTO(updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        customerGroupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
