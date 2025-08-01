package ra.edu.project_customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.project_customer.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role createRole(Role role);
    Role updateRole(Integer id, Role updatedRole);
    void deleteRole(Integer id);
    Page<Role> getRolesWithFilter(String keyword, Pageable pageable);
}
