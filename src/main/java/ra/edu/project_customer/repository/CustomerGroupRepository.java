package ra.edu.project_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.project_customer.entity.CustomerGroup;

import java.util.List;

public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Integer> {
    List<CustomerGroup> findByStatusTrue();

}