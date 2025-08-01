package ra.edu.project_customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.service.ManageUserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final ManageUserService manageUserService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(manageUserService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.of(manageUserService.getUserById(id));
    }

    @PutMapping("/{id}/update-info")
    public ResponseEntity<User> updateUserInfo(@PathVariable Integer id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(manageUserService.updateUserInfo(id, updatedUser));
    }

    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleUserStatus(@PathVariable Integer id) {
        manageUserService.toggleUserActive(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Integer id) {
        manageUserService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}