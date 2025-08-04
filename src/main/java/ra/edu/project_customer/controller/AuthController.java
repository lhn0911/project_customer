package ra.edu.project_customer.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.dto.request.*;
import ra.edu.project_customer.dto.response.APIResponse;
import ra.edu.project_customer.dto.response.JWTResponse;
import ra.edu.project_customer.dto.response.UserProfileDTO;
import ra.edu.project_customer.dto.response.UserResponseDTO;
import ra.edu.project_customer.entity.RefreshToken;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.mapper.UserMapper;
import ra.edu.project_customer.repository.UserRepository;
import ra.edu.project_customer.security.jwt.JWTProvider;
import ra.edu.project_customer.security.pricipal.CustomUserDetails;
import ra.edu.project_customer.service.OtpService;
import ra.edu.project_customer.service.RefreshTokenService;
import ra.edu.project_customer.service.UserService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final OtpService otpService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserResponseDTO>> registerUser(@Valid @RequestBody UserRegister userRegister) {
        User savedUser = userService.registerUser(userRegister);

        otpService.generateAndSendOtp(savedUser);

        UserResponseDTO dto = UserMapper.toDTO(savedUser);

        return ResponseEntity.ok(APIResponse.success(dto, "Đăng ký thành công. Vui lòng kiểm tra email để nhận mã OTP."));
    }


    @PostMapping("/otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerify dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        if (!otpService.verifyOtp(user, dto.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mã OTP không đúng hoặc đã hết hạn");
        }

        user.setEmailVerified(true);
        log.info("✅ Xác minh OTP thành công cho user: {}", user.getUsername());
        log.info("Trạng thái emailVerified: {}", user.getEmailVerified());
        userRepository.save(user);

        return ResponseEntity.ok("Xác minh OTP thành công. Bạn có thể đăng nhập.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLogin userLogin, HttpServletRequest request) {
        log.info("🔐 Đăng nhập với username: {}", userLogin.getUsername());
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
        );

        User user = userRepository.findByUsername(userLogin.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        if (!user.getEmailVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản chưa được xác minh email.");
        }

        String accessToken = jwtProvider.generateToken(user.getUsername());
        String ip = request.getRemoteAddr();
        refreshTokenService.manageRefreshTokenLimit(user, ip);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, ip);

        return ResponseEntity.ok(JWTResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .authorities(auth.getAuthorities())
                .token(accessToken)
                .refreshToken(refreshToken.getToken())
                .build());

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String refreshTokenStr = body.get("refreshToken");
        String ip = request.getRemoteAddr();

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));

        if (!refreshTokenService.isValid(refreshToken, ip)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ hoặc IP không khớp");
        }

        String accessToken = jwtProvider.generateToken(refreshToken.getUser().getUsername());
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return ResponseEntity.ok(new UserProfileDTO(
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getEmailVerified(),
                user.getUserRoles().stream()
                        .map(userRole -> userRole.getRole().getRoleName().name())
                        .toList()

        ));

    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User user = userService.getCurrentUser(); // Đã khai báo ở đây

        boolean emailChanged = request.getNewEmail() != null &&
                !request.getNewEmail().equals(user.getEmail()); // Sửa 'currentUser' thành 'user'

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        if (emailChanged) {
            user.setEmail(request.getNewEmail());
            user.setEmailVerified(false);
            otpService.generateAndSendOtp(user);
            userRepository.save(user);

            return ResponseEntity.ok("Thông tin cập nhật. Vui lòng xác minh email mới.");
        }

        userRepository.save(user);
        return ResponseEntity.ok("Thông tin cá nhân đã được cập nhật.");
    }



    // ===== 7. ĐỔI MẬT KHẨU =====
    @PutMapping("/profile/change-password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest dto
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu cũ không đúng");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }
}