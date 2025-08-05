package ra.edu.project_customer.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRQ {

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Role ID không được để trống")
    private Integer roleId;
}
