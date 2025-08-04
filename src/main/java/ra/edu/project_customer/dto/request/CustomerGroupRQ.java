package ra.edu.project_customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerGroupRQ {
    private Integer groupId;

    @NotBlank(message = "Tên nhóm không được để trống")
    private String groupName;

    private String description;
}