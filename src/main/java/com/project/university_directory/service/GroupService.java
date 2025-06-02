package com.project.university_directory.service;

import com.project.university_directory.model.student_model.Group;
import com.project.university_directory.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public void saveNewGroup(Group newGroup) {
        if (newGroup != null && newGroup.getName() != null ) {

            List<String> groups = groupRepository.findAll().stream()
                    .map(g -> new String(g.getCourse() + "" + g.getGroupNumber() + g.getName()))
                    .toList();

            newGroup.setGroupNumber((short) 1);
            newGroup.setCourse((short) 1);

            while (groups.contains(
                    new String(newGroup.getCourse() + "" + newGroup.getGroupNumber() + "" + newGroup.getName()))) {
                newGroup.setGroupNumber((short) (newGroup.getGroupNumber() + 1));
            }

            groupRepository.save(newGroup);
        }
    }

    public void updateGroup(Long groupId, Group group) {
        if (group.getCourse() != 0  && !group.getName().isEmpty()  && !group.getSpeciality().isEmpty()) {

            List<String> groups = groupRepository.findAll().stream()
                    .map(g -> new String(g.getCourse() + "" + g.getGroupNumber() + g.getName()))
                    .toList();

            while (groups.contains(
                    new String(group.getCourse() + "" + group.getGroupNumber() + "" + group.getName()))) {
                group.setGroupNumber((short) (group.getGroupNumber() + 1));
            }

            Group currentGroup = groupRepository.findById(groupId)
                    .orElseThrow( () -> new RuntimeException("groups with this id " + groupId +" do not exist"));


            currentGroup.setCourse(group.getCourse());
            currentGroup.setGroupNumber(group.getGroupNumber());
            currentGroup.setName(group.getName());
            currentGroup.setSpeciality(group.getSpeciality());
            groupRepository.save(currentGroup);
        }
    }

    public List<Group> findAllWithFilter(String groupName, Short course, String speciality) {
        Short courseFilter = (course != null) ? course : null;
        return groupRepository.filterGroup(groupName, speciality, courseFilter);
    }


    public void deleteById(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}
