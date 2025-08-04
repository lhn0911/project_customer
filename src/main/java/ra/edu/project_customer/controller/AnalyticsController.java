package ra.edu.project_customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ra.edu.project_customer.dto.response.APIResponse;
import ra.edu.project_customer.dto.response.AnalyticsResponse;
import ra.edu.project_customer.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/customer-growth")
    public ResponseEntity<APIResponse<List<AnalyticsResponse.CustomerGrowthDTO>>> getCustomerGrowth() {
        List<AnalyticsResponse.CustomerGrowthDTO> data = analyticsService.getCustomerGrowthByMonth();
        return ResponseEntity.ok(APIResponse.success(data, "Thống kê khách hàng theo tháng"));
    }

    @GetMapping("/segments")
    public ResponseEntity<APIResponse<List<AnalyticsResponse.CustomerSegmentDTO>>> getCustomerSegments() {
        List<AnalyticsResponse.CustomerSegmentDTO> data = analyticsService.getCustomerSegments();
        return ResponseEntity.ok(APIResponse.success(data, "Phân tích phân khúc khách hàng"));
    }

    @GetMapping("/ltv")
    public ResponseEntity<APIResponse<List<AnalyticsResponse.CustomerLTVDTO>>> getCustomerLTV() {
        List<AnalyticsResponse.CustomerLTVDTO> data = analyticsService.getCustomerLTV();
        return ResponseEntity.ok(APIResponse.success(data, "Tính giá trị vòng đời khách hàng"));
    }

    @GetMapping("/revenue")
    public ResponseEntity<APIResponse<List<AnalyticsResponse.RevenueDTO>>> getRevenue() {
        List<AnalyticsResponse.RevenueDTO> data = analyticsService.getRevenueByMonth();
        return ResponseEntity.ok(APIResponse.success(data, "Thống kê doanh thu theo tháng"));
    }

    @GetMapping("/top-customers")
    public ResponseEntity<APIResponse<List<AnalyticsResponse.TopCustomerDTO>>> getTopCustomers(
            @RequestParam(defaultValue = "5") int limit) {
        List<AnalyticsResponse.TopCustomerDTO> data = analyticsService.getTopCustomers(limit);
        return ResponseEntity.ok(APIResponse.success(data, "Top khách hàng chi tiêu nhiều nhất"));
    }
}