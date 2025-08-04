package ra.edu.project_customer.mapper;

import ra.edu.project_customer.dto.request.PurchaseRequest;
import ra.edu.project_customer.dto.response.PurchaseResponse;
import ra.edu.project_customer.entity.Customer;
import ra.edu.project_customer.entity.Purchase;

import java.time.LocalDateTime;

public class PurchaseMapper {
    public static Purchase toEntity(PurchaseRequest request, Customer customer) {
        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setTotalAmount(request.getTotalAmount());
        purchase.setCurrency(request.getCurrency());
        purchase.setPaymentMethod(request.getPaymentMethod());
        purchase.setStatus(request.getStatus());
        purchase.setNotes(request.getNotes());
        purchase.setPurchaseDate(LocalDateTime.now());
        return purchase;
    }

    public static PurchaseResponse toResponse(Purchase entity) {
        return PurchaseResponse.builder()
                .purchaseId(entity.getPurchaseId())
                .customerId(entity.getCustomer().getCustomerId())
                .purchaseDate(entity.getPurchaseDate())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .build();
    }
}