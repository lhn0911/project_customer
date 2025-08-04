package ra.edu.project_customer.service;

import ra.edu.project_customer.dto.response.AnalyticsResponse;

import java.util.List;

public interface AnalyticsService {
    List<AnalyticsResponse.CustomerGrowthDTO> getCustomerGrowthByMonth();

    List<AnalyticsResponse.CustomerSegmentDTO> getCustomerSegments();

    List<AnalyticsResponse.CustomerLTVDTO> getCustomerLTV();

    List<AnalyticsResponse.RevenueDTO> getRevenueByMonth();

    List<AnalyticsResponse.TopCustomerDTO> getTopCustomers(int limit);
}