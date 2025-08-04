package ra.edu.project_customer.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.entity.Customer;
import ra.edu.project_customer.entity.CustomerStatus;
import ra.edu.project_customer.entity.RoleEnum;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.exception.BadRequestException;
import ra.edu.project_customer.exception.NotFoundException;
import ra.edu.project_customer.repository.CustomerRepository;
import ra.edu.project_customer.repository.UserRepository;
import ra.edu.project_customer.service.CustomerService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }


    @Override
    public Customer update(Integer id, Customer customer) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        existing.setUser(customer.getUser());
        existing.setGroup(customer.getGroup());
        existing.setAddress(customer.getAddress());
        existing.setCity(customer.getCity());
        existing.setCountry(customer.getCountry());
        return customerRepository.save(existing);
    }

    @Override
    public void updateStatus(Integer id, String statusStr) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        CustomerStatus status = CustomerStatus.valueOf(statusStr.toUpperCase());
        customer.setStatus(status);
        customerRepository.save(customer);
    }

    @Override
    public void delete(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        customer.setIsDelete(true);
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }


    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    @Override
    public Customer findByUserId(Integer userId) {
        return customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new NotFoundException("Customer with userId not found"));
    }

    @Override
    public Page<Customer> findAll(String name, Pageable pageable) {
        return customerRepository.findAllByUser_FullNameContainingIgnoreCaseAndIsDeleteFalse(name, pageable);
    }
}
