package ra.edu.project_customer.mapper;

import ra.edu.project_customer.dto.request.UserRoleRQ;
import ra.edu.project_customer.dto.response.UserRoleDTO;
import ra.edu.project_customer.entity.Role;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.entity.UserRole;

import java.time.LocalDateTime;

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

    public static UserRole toEntity(UserRoleRQ rq, User user, Role role) {
        return UserRole.builder()
                .user(user)
                .role(role)
                .assignedAt(LocalDateTime.now())
                .build();
    }
}
