package ra.edu.project_customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.edu.project_customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUser_UserId(Integer userId);

    Page<Customer> findAllByUser_FullNameContainingIgnoreCaseAndIsDeleteFalse(String name, Pageable pageable);

    // tăng trưởng khách hàng
    @Query("SELECT FUNCTION('DATE_FORMAT', c.createdAt, '%Y-%m'), COUNT(c) " +
            "FROM Customer c WHERE c.isDelete = false " +
            "GROUP BY FUNCTION('DATE_FORMAT', c.createdAt, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', c.createdAt, '%Y-%m') ASC")
    List<Object[]> countCustomerGrowthByMonth();


    // phân tích phân khúc
    @Query("SELECT c.group.groupName, COUNT(c) FROM Customer c WHERE c.isDelete = false GROUP BY c.group.groupName")
    List<Object[]> countCustomersByGroup();

    //tính life-time
    @Query("SELECT c.customerId, c.user.fullName, SUM(p.totalAmount) FROM Purchase p " +
            "JOIN p.customer c WHERE c.isDelete = false GROUP BY c.customerId, c.user.fullName")
    List<Object[]> calculateCustomerLTV();

    // doanh thu theo time
    @Query("SELECT FUNCTION('DATE_FORMAT', p.purchaseDate, '%Y-%m'), SUM(p.totalAmount) " +
            "FROM Purchase p WHERE p.status = 'COMPLETED' " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.purchaseDate, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', p.purchaseDate, '%Y-%m') ASC")
    List<Object[]> getRevenueByMonth();


    // khách hàng mua nhiều nhất
    @Query("SELECT p.customer.user.fullName, SUM(p.totalAmount) AS total FROM Purchase p " +
            "WHERE p.status = 'COMPLETED' AND p.customer.isDelete = false " +
            "GROUP BY p.customer.user.fullName ORDER BY total DESC")
    List<Object[]> findTopCustomers(Pageable pageable);



}