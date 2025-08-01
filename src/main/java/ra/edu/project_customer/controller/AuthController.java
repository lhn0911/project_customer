package ra.edu.project_customer.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
import ra.edu.project_customer.entity.RefreshToken;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.repository.UserRepository;
import ra.edu.project_customer.security.jwt.JWTProvider;
import ra.edu.project_customer.security.pricipal.CustomUserDetails;
import ra.edu.project_customer.service.OtpService;
import ra.edu.project_customer.service.RefreshTokenService;
import ra.edu.project_customer.service.UserService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // ===== 1. ĐĂNG KÝ =====
    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> registerUser(@RequestBody UserRegister userRegister) {
        User createdUser = userService.registerUser(userRegister); // Xử lý gửi OTP ở đây
        return ResponseEntity.ok(APIResponse.success(createdUser, "Đăng ký thành công. Vui lòng kiểm tra email để xác thực."));
    }

    // ===== 2. XÁC MINH OTP =====
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyDTO dto, HttpServletRequest request) {
        // Xác thực tài khoản + mật khẩu
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();

        // Kiểm tra OTP
        if (!otpService.verifyOtp(user, dto.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mã OTP không đúng");
        }

        // Đánh dấu tài khoản đã xác minh
        user.setVerified(true);
        userRepository.save(user);

        // Tạo access token & refresh token
        String accessToken = jwtProvider.generateToken(user.getUsername());
        String ip = request.getRemoteAddr();
        refreshTokenService.manageRefreshTokenLimit(user, ip);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, ip);

        return ResponseEntity.ok(new JWTResponse(accessToken, refreshToken.getToken()));
    }

    // ===== 3. ĐĂNG NHẬP =====
    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> loginUser(@RequestBody UserLogin userLogin) {
        // Lấy user
        User user = userRepository.findByUsername(userLogin.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        // Kiểm tra đã xác minh OTP chưa
        if (!user.isVerified()) {
            throw new RuntimeException("Tài khoản chưa xác minh OTP. Vui lòng kiểm tra email.");
        }

        // Gọi service xử lý đăng nhập và sinh token
        JWTResponse jwtResponse = userService.login(userLogin);
        return ResponseEntity.ok(APIResponse.success(jwtResponse, "Đăng nhập thành công"));
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
                user.getEmailVerified()
        ));

    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody UpdateProfileRequest dto) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }
    @PutMapping("/profile/change-password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest dto
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
