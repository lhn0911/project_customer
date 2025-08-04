package ra.edu.project_customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.project_customer.dto.response.UserRoleDTO;

public interface UserRoleService {
    Page<UserRoleDTO> getAllUserRoles(String username, String roleName, Pageable pageable);
    UserRoleDTO getUserRoleById(Integer userRoleId);
    UserRoleDTO assignRoleToUser(Integer userId, Integer roleId);
    UserRoleDTO updateUserRole(Integer userRoleId, Integer newRoleId);
    void revokeUserRole(Integer userRoleId);

}
