package ra.edu.project_customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.dto.request.UserRoleRQ;
import ra.edu.project_customer.dto.response.UserRoleDTO;
import ra.edu.project_customer.service.UserRoleService;

@RestController
@RequestMapping("/api/v1/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<Page<UserRoleDTO>> getAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String roleName,
            Pageable pageable) {
        return ResponseEntity.ok(userRoleService.getAllUserRoles(username, roleName, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(userRoleService.getUserRoleById(id));
    }

    @PostMapping("/assign")
    public ResponseEntity<UserRoleDTO> assign(@RequestBody @Valid UserRoleRQ request) {
        return ResponseEntity.ok(
                userRoleService.assignRoleToUser(request.getUserId(), request.getRoleId())
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UserRoleRQ request) {
        return ResponseEntity.ok(
                userRoleService.updateUserRole(id, request.getRoleId())
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Integer id) {
        userRoleService.revokeUserRole(id);
        return ResponseEntity.noContent().build();
    }

}

