package ra.edu.project_customer.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.entity.CustomerGroup;
import ra.edu.project_customer.exception.ResourceNotFoundException;
import ra.edu.project_customer.repository.CustomerGroupRepository;
import ra.edu.project_customer.service.CustomerGroupService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerGroupServiceImpl implements CustomerGroupService {

    private final CustomerGroupRepository groupRepository;

    @Override
    public List<CustomerGroup> getAllGroups() {
        return groupRepository.findByStatusTrue();
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
    public CustomerGroup findById(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + groupId));
    }

    @Override
    public void deleteGroup(Integer id) {
        CustomerGroup group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: " + id));

        group.setStatus(false);
        group.setUpdatedAt(LocalDateTime.now());

        groupRepository.save(group);
    }

}
