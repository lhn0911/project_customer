package ra.edu.project_customer.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.entity.CustomerGroup;
import ra.edu.project_customer.exception.ResourceNotFoundException;
import ra.edu.project_customer.repository.CustomerGroupRepository;
import ra.edu.project_customer.service.CustomerGroupService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerGroupServiceImpl implements CustomerGroupService {

    private final CustomerGroupRepository groupRepository;

    @Override
    public List<CustomerGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public CustomerGroup createGroup(CustomerGroup group) {
        return groupRepository.save(group);
    }

    @Override
    public CustomerGroup updateGroup(Integer id, CustomerGroup updatedGroup) {
        CustomerGroup existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));

        existingGroup.setGroupName(updatedGroup.getGroupName());
        existingGroup.setDescription(updatedGroup.getDescription());

        return groupRepository.save(existingGroup);
    }

    @Override
    public void deleteGroup(Integer id) {
        if (!groupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer group not found with id: " + id);
        }
        groupRepository.deleteById(id);
    }
}
