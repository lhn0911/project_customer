package ra.edu.project_customer.service;

import ra.edu.project_customer.dto.request.PurchaseRequest;
import ra.edu.project_customer.dto.response.PurchaseResponse;

import java.util.List;

public interface PurchaseService {
    List<PurchaseResponse> getPurchasesByCustomerId(Integer customerId);

    PurchaseResponse createPurchase(Integer customerId, PurchaseRequest request);

    PurchaseResponse updatePurchase(Integer customerId, Integer purchaseId, PurchaseRequest request);

    void deletePurchase(Integer customerId, Integer purchaseId);
}
