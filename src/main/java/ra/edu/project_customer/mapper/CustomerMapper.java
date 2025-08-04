package ra.edu.project_customer.mapper;

import jakarta.validation.Valid;
import ra.edu.project_customer.dto.request.CustomerRQ;
import ra.edu.project_customer.dto.response.CustomerResponse;
import ra.edu.project_customer.entity.Customer;
import ra.edu.project_customer.entity.CustomerGroup;
import ra.edu.project_customer.entity.User;

import java.time.LocalDateTime;

public class CustomerMapper {
    public static Customer toEntity(CustomerRQ dto, User user, CustomerGroup group) {
        return Customer.builder()
                .customerId(dto.getCustomerId())
                .user(user)
                .group(group)
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .status(dto.getStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .userId(customer.getUser() != null ? customer.getUser().getUserId() : null)
                .fullName(customer.getUser() != null ? customer.getUser().getFullName() : null)
                .groupId(customer.getGroup() != null ? customer.getGroup().getGroupId() : null)
                .groupName(customer.getGroup() != null ? customer.getGroup().getGroupName() : null)
                .address(customer.getAddress())
                .city(customer.getCity())
                .country(customer.getCountry())
                .status(customer.getStatus() != null ? customer.getStatus().name() : null)
                .isDelete(customer.getIsDelete() ? "Đã xóa" : "Chưa xóa")
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }




}
