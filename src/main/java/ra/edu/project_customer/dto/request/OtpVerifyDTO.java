package ra.edu.project_customer.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OtpVerifyDTO {
    private String username;
    private String password;
    private String otp;
}