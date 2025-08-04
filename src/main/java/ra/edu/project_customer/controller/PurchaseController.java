package ra.edu.project_customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.dto.request.PurchaseRequest;
import ra.edu.project_customer.dto.response.PurchaseResponse;
import ra.edu.project_customer.service.PurchaseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping("/{id}/purchases")
    public ResponseEntity<List<PurchaseResponse>> getPurchases(@PathVariable Integer id) {
        return ResponseEntity.ok(purchaseService.getPurchasesByCustomerId(id));
    }

    @PostMapping("/{id}/purchases")
    public ResponseEntity<PurchaseResponse> addPurchase(@PathVariable Integer id, @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(purchaseService.createPurchase(id, request));
    }

    @PutMapping("/{id}/purchases/{purchaseId}")
    public ResponseEntity<PurchaseResponse> updatePurchase(@PathVariable Integer id, @PathVariable Integer purchaseId, @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(purchaseService.updatePurchase(id, purchaseId, request));
    }

    @DeleteMapping("/{id}/purchases/{purchaseId}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Integer id, @PathVariable Integer purchaseId) {
        purchaseService.deletePurchase(id, purchaseId);
        return ResponseEntity.noContent().build();
    }
}
