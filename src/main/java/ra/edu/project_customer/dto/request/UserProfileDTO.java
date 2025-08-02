package ra.edu.project_customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean emailVerified;
    private List<String> roles;
}
