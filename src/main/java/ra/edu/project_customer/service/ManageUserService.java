package ra.edu.project_customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.project_customer.entity.User;

import java.util.Optional;

public interface ManageUserService {
    Page<User> getAllUsers(Pageable pageable);
    Optional<User> getUserById(Integer id);
    User updateUserInfo(Integer id, User user);
    void toggleUserActive(Integer id);
    void softDeleteUser(Integer id);
}