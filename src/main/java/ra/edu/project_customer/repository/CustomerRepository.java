package ra.edu.project_customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.project_customer.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUser_UserId(Integer userId);

    Page<Customer> findAllByUser_FullNameContainingIgnoreCase(String name, Pageable pageable);
}