package ra.edu.project_customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ra.edu.project_customer.entity.CustomerStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerRQ {
    private Integer customerId;

    private Integer userId;

    private Integer groupId;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Thành phố không được để trống")
    private String city;

    @NotBlank(message = "Quốc gia không được để trống")
    private String country;

    private CustomerStatus status;
}
