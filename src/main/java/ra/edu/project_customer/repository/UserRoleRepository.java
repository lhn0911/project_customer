package ra.edu.project_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ra.edu.project_customer.entity.Role;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.entity.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {
    Optional<UserRole> findByUser_UserIdAndRole_RoleId(Integer userId, Integer roleId);
    boolean existsByUserAndRole(User user, Role role);
}
