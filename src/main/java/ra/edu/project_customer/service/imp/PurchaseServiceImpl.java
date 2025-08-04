package ra.edu.project_customer.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.project_customer.dto.request.PurchaseRequest;
import ra.edu.project_customer.dto.response.PurchaseResponse;
import ra.edu.project_customer.entity.Customer;
import ra.edu.project_customer.entity.Purchase;
import ra.edu.project_customer.exception.NotFoundException;
import ra.edu.project_customer.mapper.PurchaseMapper;
import ra.edu.project_customer.repository.CustomerRepository;
import ra.edu.project_customer.repository.PurchaseRepository;
import ra.edu.project_customer.service.PurchaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;

    private Customer getCustomerOrThrow(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    }

    private Purchase getPurchaseOrThrow(Integer purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + purchaseId));
    }

    @Override
    public List<PurchaseResponse> getPurchasesByCustomerId(Integer customerId) {
        getCustomerOrThrow(customerId);
        return purchaseRepository.findByCustomer_CustomerId(customerId).stream()
                .map(PurchaseMapper::toResponse)
                .toList();
    }

    @Override
    public PurchaseResponse createPurchase(Integer customerId, PurchaseRequest request) {
        Customer customer = getCustomerOrThrow(customerId);
        Purchase purchase = PurchaseMapper.toEntity(request, customer);
        return PurchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    public PurchaseResponse updatePurchase(Integer customerId, Integer purchaseId, PurchaseRequest request) {
        getCustomerOrThrow(customerId);
        Purchase purchase = getPurchaseOrThrow(purchaseId);

        if (!purchase.getCustomer().getCustomerId().equals(customerId)) {
            throw new NotFoundException("Purchase does not belong to customer.");
        }

        purchase.setTotalAmount(request.getTotalAmount());
        purchase.setCurrency(request.getCurrency());
        purchase.setPaymentMethod(request.getPaymentMethod());
        purchase.setStatus(request.getStatus());
        purchase.setNotes(request.getNotes());

        return PurchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    public void deletePurchase(Integer customerId, Integer purchaseId) {
        getCustomerOrThrow(customerId);
        Purchase purchase = getPurchaseOrThrow(purchaseId);
        if (!purchase.getCustomer().getCustomerId().equals(customerId)) {
            throw new NotFoundException("Purchase does not belong to customer.");
        }
        purchaseRepository.delete(purchase);
    }
}
