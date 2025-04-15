package com.example.tmbackend.controller;

import com.example.tmbackend.dto.GroupCreateDTO;
import com.example.tmbackend.dto.GroupResponseDTO;
import com.example.tmbackend.model.Group;
import com.example.tmbackend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task-master/groups")
@CrossOrigin(origins = "http://localhost:4200")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public GroupResponseDTO createGroup(@RequestBody GroupCreateDTO dto) {
        return groupService.createGroupFromDTO(dto);
    }

    @GetMapping
    public List<GroupResponseDTO> getAllGroups() {
        return groupService.getAllGroups().stream()
                .map(groupService::toDTO)
                .collect(Collectors.toList());
    }


    @GetMapping("/admin/{adminId}")
    public List<GroupResponseDTO> getGroupsByAdmin(@PathVariable Integer adminId) {
        return groupService.getGroupsByAdminDTO(adminId);
    }

    @GetMapping("/member/{memberId}")
    public List<GroupResponseDTO> getGroupsByMember(@PathVariable Integer memberId) {
        return groupService.getGroupsByMemberDTO(memberId);
    }

    @GetMapping("/{id}")
    public GroupResponseDTO getGroupById(@PathVariable Integer id) {
        Group group = groupService.getGroupById(id);
        return groupService.toDTO(group);
    }


    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Integer id) {
        groupService.deleteGroup(id);
    }

    @PutMapping("/{id}/add-members")
    public ResponseEntity<Void> addMembersToGroup(@PathVariable Integer id, @RequestBody List<Integer> newMemberIds) {
        groupService.addMembers(id, newMemberIds);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/remove-members")
    public GroupResponseDTO removeMembersFromGroup(@PathVariable Integer id, @RequestBody List<Integer> memberIdsToRemove) {
        Group updatedGroup = groupService.removeMembers(id, memberIdsToRemove);
        return groupService.toDTO(updatedGroup);
    }


    @PutMapping("/{id}/rename")
    public GroupResponseDTO renameGroup(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String newName = body.get("name");
        return groupService.renameGroup(id, newName);
    }


    @GetMapping("/search")
    public List<GroupResponseDTO> searchGroupsByName(@RequestParam String name) {
        return groupService.searchGroupsByName(name);
    }


}
