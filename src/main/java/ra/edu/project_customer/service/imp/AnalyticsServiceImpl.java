package ra.edu.project_customer.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.dto.response.AnalyticsResponse;
import ra.edu.project_customer.repository.CustomerRepository;
import ra.edu.project_customer.service.AnalyticsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final CustomerRepository customerRepository;

    @Override
    public List<AnalyticsResponse.CustomerGrowthDTO> getCustomerGrowthByMonth() {
        return customerRepository.countCustomerGrowthByMonth()
                .stream()
                .map(obj -> new AnalyticsResponse.CustomerGrowthDTO(
                        (String) obj[0],
                        (Long) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsResponse.CustomerSegmentDTO> getCustomerSegments() {
        return customerRepository.countCustomersByGroup()
                .stream()
                .map(obj -> new AnalyticsResponse.CustomerSegmentDTO(
                        (String) obj[0],
                        (Long) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsResponse.CustomerLTVDTO> getCustomerLTV() {
        return customerRepository.calculateCustomerLTV()
                .stream()
                .map(obj -> new AnalyticsResponse.CustomerLTVDTO(
                        (Integer) obj[0],
                        (String) obj[1],
                        (BigDecimal) obj[2]))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsResponse.RevenueDTO> getRevenueByMonth() {
        return customerRepository.getRevenueByMonth()
                .stream()
                .map(obj -> new AnalyticsResponse.RevenueDTO(
                        (String) obj[0],
                        (BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsResponse.TopCustomerDTO> getTopCustomers(int limit) {
        return customerRepository.findTopCustomers(PageRequest.of(0, limit))
                .stream()
                .map(obj -> new AnalyticsResponse.TopCustomerDTO(
                        (String) obj[0],
                        (BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }
}