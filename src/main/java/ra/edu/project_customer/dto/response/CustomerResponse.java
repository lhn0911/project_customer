package ra.edu.project_customer.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Integer customerId;
    private Integer userId;
    private String fullName;
    private Integer groupId;
    private String groupName;
    private String address;
    private String city;
    private String country;
    private String status;
    private String isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

