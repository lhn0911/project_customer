package ra.edu.project_customer.mapper;

import ra.edu.project_customer.dto.request.CustomerRQ;
import ra.edu.project_customer.entity.Customer;
import ra.edu.project_customer.entity.CustomerGroup;
import ra.edu.project_customer.entity.User;

public class CustomerMapper {
    public static CustomerRQ toDTO(Customer entity) {
        CustomerRQ dto = new CustomerRQ();
        dto.setCustomerId(entity.getCustomerId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setGroupId(entity.getGroup() != null ? entity.getGroup().getGroupId() : null);
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setCountry(entity.getCountry());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public static Customer toEntity(Customer dto, User user, CustomerGroup group) {
        Customer entity = new Customer();
        entity.setCustomerId(dto.getCustomerId());
        entity.setUser(user);
        entity.setGroup(group);
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setCountry(dto.getCountry());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : entity.getStatus());
        return entity;
    }
}
