package ra.edu.project_customer.mapper;

import ra.edu.project_customer.dto.request.ManagerUDUser;
import ra.edu.project_customer.entity.User;

public class UDUserMapper {

    public static ManagerUDUser toDTO(User user) {
        if (user == null) return null;

        return ManagerUDUser.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.getIsActive())
                .build();
    }

    public static void updateEntityFromDTO(ManagerUDUser dto, User user) {
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsActive(dto.getIsActive());
    }
}
