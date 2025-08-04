package ra.edu.project_customer.service;


import ra.edu.project_customer.entity.CustomerGroup;

import java.util.List;

public interface CustomerGroupService {
    List<CustomerGroup> getAllGroups();

    CustomerGroup createGroup(CustomerGroup dto);

    CustomerGroup updateGroup(Integer id, CustomerGroup cus);

    void deleteGroup(Integer id);
}