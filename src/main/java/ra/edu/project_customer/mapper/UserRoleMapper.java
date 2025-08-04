package ra.edu.project_customer.mapper;

import ra.edu.project_customer.dto.response.UserRoleDTO;
import ra.edu.project_customer.entity.UserRole;

public class UserRoleMapper {
    public static UserRoleDTO toDTO(UserRole ur) {
        return UserRoleDTO.builder()
                .userRoleId(ur.getUserRoleId())
                .userId(ur.getUser().getUserId())
                .username(ur.getUser().getUsername())
                .roleId(ur.getRole().getRoleId())
                .roleName(ur.getRole().getRoleName().name())
                .assignedAt(ur.getAssignedAt())
                .build();
    }
}
