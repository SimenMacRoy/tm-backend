package com.example.tmbackend.service;

import com.example.tmbackend.dto.GroupCreateDTO;
import com.example.tmbackend.dto.GroupResponseDTO;
import com.example.tmbackend.exceptions.NotFoundException;
import com.example.tmbackend.model.Group;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.GroupRepository;
import com.example.tmbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public GroupResponseDTO createGroupFromDTO(GroupCreateDTO dto) {
        User admin = userRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new NotFoundException("Admin introuvable"));

        List<User> members = (List<User>) userRepository.findAllById(dto.getMemberIds());

        Group group = new Group();
        group.setName(dto.getName());
        group.setDateCreation(new Date()); // ou `new Date()` si c'est un Date
        group.setAdmin(admin);
        group.setMembers(members);

        Group savedGroup = groupRepository.save(group);

        return toDTO(savedGroup); // méthode de conversion vers GroupResponseDTO
    }


    public List<Group> getAllGroups() {
        return (List<Group>) groupRepository.findAll();
    }

    public Group getGroupById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Groupe introuvable"));
    }

    public void deleteGroup(Integer id) {
        if (!groupRepository.existsById(id))
            throw new NotFoundException("Groupe introuvable pour suppression");

        groupRepository.deleteById(id);
    }

    public Group addMembers(Integer groupId, List<Integer> newMemberIds) {
        Group group = getGroupById(groupId);
        List<User> newMembers = (List<User>) userRepository.findAllById(newMemberIds);
        Set<User> allMembers = new HashSet<>(group.getMembers());
        allMembers.addAll(newMembers);
        group.setMembers(new ArrayList<>(allMembers));
        return groupRepository.save(group);
    }

    public Group removeMembers(Integer groupId, List<Integer> memberIdsToRemove) {
        Group group = getGroupById(groupId);
        List<User> updatedMembers = group.getMembers();
        updatedMembers.removeIf(user -> memberIdsToRemove.contains(user.getId()));
        group.setMembers(updatedMembers);
        return groupRepository.save(group);
    }

    public List<Group> getGroupsByAdmin(Integer adminId) {
        return groupRepository.findByAdminId(adminId);
    }

    public List<Group> getGroupsByMember(Integer memberId) {
        return StreamSupport.stream(groupRepository.findAll().spliterator(), false)
                .filter(group -> group.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(memberId)))
                .collect(Collectors.toList());
    }

    public List<GroupResponseDTO> getGroupsByAdminDTO(Integer adminId) {
        return getGroupsByAdmin(adminId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<GroupResponseDTO> getGroupsByMemberDTO(Integer memberId) {
        return getGroupsByMember(memberId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public GroupResponseDTO toDTO(Group group) {
        GroupResponseDTO dto = new GroupResponseDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDateCreation(group.getDateCreation());
        dto.setAdminId(group.getAdmin() != null ? group.getAdmin().getId() : null);
        dto.setMembers(
                group.getMembers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }

}
