package ra.edu.project_customer.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import ra.edu.project_customer.entity.PurchaseStatus;

import java.math.BigDecimal;

@Data
public class PurchaseRequest {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @NotBlank
    private String currency = "VND";

    @NotBlank
    private String paymentMethod;

    @NotNull
    private PurchaseStatus status = PurchaseStatus.COMPLETED;

    private String notes;
}