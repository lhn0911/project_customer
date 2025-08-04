package ra.edu.project_customer.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    private Integer userRoleId;
    private Integer userId;
    private String username;
    private Integer roleId;
    private String roleName;
    private LocalDateTime assignedAt;
}
