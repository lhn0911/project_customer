package ra.edu.project_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.project_customer.entity.Purchase;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByCustomer_CustomerId(Integer customerId);
}
