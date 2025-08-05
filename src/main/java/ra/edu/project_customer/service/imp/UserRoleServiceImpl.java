package ra.edu.project_customer.service.imp;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.dto.request.UserRoleRQ;
import ra.edu.project_customer.dto.response.UserRoleDTO;
import ra.edu.project_customer.entity.Role;
import ra.edu.project_customer.entity.RoleEnum;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.entity.UserRole;
import ra.edu.project_customer.mapper.UserRoleMapper;
import ra.edu.project_customer.repository.RoleRepository;
import ra.edu.project_customer.repository.UserRepository;
import ra.edu.project_customer.repository.UserRoleRepository;
import ra.edu.project_customer.service.UserRoleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Page<UserRoleDTO> getAllUserRoles(String username, String roleName, Pageable pageable) {
        return userRoleRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("user").get("username")), "%" + username.toLowerCase() + "%"));
            }

            if (roleName != null && !roleName.isEmpty()) {
                predicates.add(cb.equal(root.get("role").get("roleName"), RoleEnum.valueOf(roleName.toUpperCase())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(UserRoleMapper::toDTO);
    }

    @Override
    public UserRoleDTO getUserRoleById(Integer userRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân quyền với ID: " + userRoleId));
        return UserRoleMapper.toDTO(userRole);
    }

    @Override
    public UserRoleDTO assignRoleToUser(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        boolean exists = userRoleRepository.existsByUserAndRole(user, role);
        if (exists) {
            throw new RuntimeException("User đã có role này rồi");
        }

        UserRole userRole = UserRoleMapper.toEntity(new UserRoleRQ(userId, roleId), user, role);
        userRole = userRoleRepository.save(userRole);

        return UserRoleMapper.toDTO(userRole);
    }


    @Override
    public UserRoleDTO updateUserRole(Integer userRoleId, Integer newRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân quyền"));

        Role newRole = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new RuntimeException("Role mới không tồn tại"));

        userRole.setRole(newRole);
        return UserRoleMapper.toDTO(userRoleRepository.save(userRole));
    }

    @Override
    public void revokeUserRole(Integer userRoleId) {
        if (!userRoleRepository.existsById(userRoleId)) {
            throw new RuntimeException("Không tìm thấy phân quyền");
        }
        userRoleRepository.deleteById(userRoleId);
    }


}

