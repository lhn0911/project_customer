package ra.edu.project_customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.dto.request.ManagerUDUser;
import ra.edu.project_customer.dto.response.UserResponseDTO;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.mapper.UDUserMapper;
import ra.edu.project_customer.mapper.UserMapper;
import ra.edu.project_customer.service.ManageUserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final ManageUserService manageUserService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = manageUserService.getAllUsers(pageable);

        Page<UserResponseDTO> dtoPage = users.map(UserMapper::toDTO);

        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        Optional<User> optionalUser = manageUserService.getUserById(id);
        return optionalUser.map(user -> ResponseEntity.ok(UserMapper.toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}/update-info")
    public ResponseEntity<ManagerUDUser> updateUserInfo(
            @PathVariable Integer id,
            @Valid @RequestBody ManagerUDUser updatedUserDto
    ) {
        User user = manageUserService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
        UDUserMapper.updateEntityFromDTO(updatedUserDto, user);
        User updatedUser = manageUserService.updateUserInfo(id, user);
        return ResponseEntity.ok(UDUserMapper.toDTO(updatedUser));
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