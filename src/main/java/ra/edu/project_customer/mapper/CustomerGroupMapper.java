package ra.edu.project_customer.mapper;

import ra.edu.project_customer.dto.request.CustomerGroupRQ;

import ra.edu.project_customer.dto.request.CustomerGroupRQ;
import ra.edu.project_customer.dto.response.CustomerGroupDTO;
import ra.edu.project_customer.entity.CustomerGroup;

public class CustomerGroupMapper {

    public static CustomerGroup toEntity(CustomerGroupRQ rq) {
        CustomerGroup entity = new CustomerGroup();
        entity.setGroupName(rq.getGroupName());
        entity.setDescription(rq.getDescription());
        return entity;
    }

    public static CustomerGroupDTO toDTO(CustomerGroup entity) {
        CustomerGroupDTO dto = new CustomerGroupDTO();
        dto.setGroupId(entity.getGroupId());
        dto.setGroupName(entity.getGroupName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
