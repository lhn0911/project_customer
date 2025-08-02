package ra.edu.project_customer.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank
    @Size(max = 100)
    private String fullName;

    @Size(max = 20)
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String newEmail;
}
