package ra.edu.project_customer.dto.response;

import lombok.Builder;
import lombok.Data;
import ra.edu.project_customer.entity.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseResponse {
    private Integer purchaseId;
    private Integer customerId;
    private LocalDateTime purchaseDate;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentMethod;
    private PurchaseStatus status;
    private String notes;
}