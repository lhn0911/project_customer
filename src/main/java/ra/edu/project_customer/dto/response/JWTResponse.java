package ra.edu.project_customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ra.edu.project_customer.entity.User;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTResponse {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Collection<? extends GrantedAuthority> authorities;
    private String token;

    private String refreshToken;


    public JWTResponse(User user, String accessToken, String refreshToken, Collection<? extends GrantedAuthority> authorities) {
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.authorities = authorities;
    }

}
