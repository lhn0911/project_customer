package ra.edu.project_customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.project_customer.entity.Customer;

public interface CustomerService {
    Customer save(Customer customer);

    Customer update(Integer id, Customer customer);

    void updateStatus(Integer id, String status);

    void delete(Integer id);

    Customer findById(Integer id);

    Customer findByUserId(Integer userId);

    Page<Customer> findAll(String name, Pageable pageable);
}
