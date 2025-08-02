    package ra.edu.project_customer.service.imp;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import ra.edu.project_customer.dto.request.UserLogin;
    import ra.edu.project_customer.dto.request.UserRegister;
    import ra.edu.project_customer.dto.response.JWTResponse;
    import ra.edu.project_customer.entity.Role;
    import ra.edu.project_customer.entity.RoleEnum;
    import ra.edu.project_customer.entity.User;
    import ra.edu.project_customer.entity.UserRole;
    import ra.edu.project_customer.repository.RoleRepository;
    import ra.edu.project_customer.repository.UserRepository;
    import ra.edu.project_customer.security.jwt.JWTProvider;
    import ra.edu.project_customer.security.pricipal.CustomUserDetails;
    import ra.edu.project_customer.service.OtpService;
    import ra.edu.project_customer.service.UserService;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.stream.Collectors;

    @Service
    @Slf4j
    public class UserServiceImp implements UserService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private JWTProvider jwtProvider;
        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private OtpService otpService;

        @Override
        public User registerUser(UserRegister userRegister) {
            if (userRepository.existsByUsername(userRegister.getUsername())) {
                throw new RuntimeException("Username đã tồn tại");
            }

            if (userRepository.existsByEmail(userRegister.getEmail())) {
                throw new RuntimeException("Email đã tồn tại");
            }

            User user = User.builder()
                    .username(userRegister.getUsername())
                    .passwordHash(passwordEncoder.encode(userRegister.getPassword()))
                    .email(userRegister.getEmail())
                    .fullName(userRegister.getFullName())
                    .phoneNumber(userRegister.getPhoneNumber())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .emailVerified(false)
                    .build();

            List<Role> roles = mapRoleStringToRole(userRegister.getRoles());
            List<UserRole> userRoles = roles.stream()
                    .map(role -> new UserRole(user, role))
                    .collect(Collectors.toList());

            user.setUserRoles(userRoles);
            return userRepository.save(user);
        }

        @Override
        public JWTResponse login(UserLogin userLogin) {
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
            } catch (AuthenticationException e) {
                log.error("Sai username hoặc password!");
                throw new RuntimeException("Đăng nhập thất bại!");
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtProvider.generateToken(userDetails.getUsername());

            return JWTResponse.builder()
                    .username(userDetails.getUsername())
                    .fullName(userDetails.getFullName())
                    .email(userDetails.getEmail())
                    .authorities(userDetails.getAuthorities())
                    .token(token)
                    .build();
        }
        @Override
        public boolean isUsernameTaken(String username) {
            return userRepository.existsByUsername(username);
        }

        @Override
        public boolean isEmailTaken(String email) {
            return userRepository.existsByEmail(email);
        }

        public User getCurrentUser() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        }


        private List<Role> mapRoleStringToRole(List<String> roles) {
            List<Role> roleList = new ArrayList<>();

            if (roles != null && !roles.isEmpty()) {
                for (String role : roles) {
                    RoleEnum roleEnum = RoleEnum.valueOf(role.toUpperCase());
                    Role foundRole = roleRepository.findByRoleName(roleEnum)
                            .orElseThrow(() -> new NoSuchElementException("Không tồn tại role: " + role));
                    roleList.add(foundRole);
                }
            } else {
                Role foundRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER)
                        .orElseThrow(() -> new NoSuchElementException("Không tồn tại role: CUSTOMER"));
                roleList.add(foundRole);
            }

            return roleList;
        }
    }