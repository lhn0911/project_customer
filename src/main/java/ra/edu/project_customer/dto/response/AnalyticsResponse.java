package ra.edu.project_customer.dto.response;

import java.math.BigDecimal;

public class AnalyticsResponse {

    // Tăng trưởng khách hàng theo tháng
    public record CustomerGrowthDTO(String month, Long count) {}

    // Phân khúc khách hàng theo nhóm
    public record CustomerSegmentDTO(String groupName, Long count) {}

    // Customer Lifetime Value
    public record CustomerLTVDTO(Integer customerId, String fullName, BigDecimal totalAmount) {}

    // Doanh thu theo tháng
    public record RevenueDTO(String month, BigDecimal totalRevenue) {}

    // Top khách hàng
    public record TopCustomerDTO(String fullName, BigDecimal totalSpent) {}
}