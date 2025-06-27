package com.example.intecproject.controller;
import com.example.intecproject.model.Group;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.intecproject.service.GroupService;
import com.example.intecproject.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController

@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }
    @GetMapping("/groups")
    public List<Group> listAllGroups() {
        return groupService.findAll();
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id)
    {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/groups/create")
    public ResponseEntity<Group> createNewGroup(@RequestBody Group group)
    {
        Group createdGroup=groupService.createGroup(group.getGroupName());
        return new ResponseEntity<>(createdGroup,HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Group> getDetailsForGroup(@PathVariable Long id)
    {
        return ResponseEntity.ok(groupService.findById(id));
    }

    @PostMapping("/api/groups/{groupId}/users/{userId}/place")
    public ResponseEntity<?> placeUserInGroup(@PathVariable Long groupId, @PathVariable Long userId) throws Exception {
        groupService.addUserToGroup(userId, groupId);
        return ResponseEntity.ok("User added to group.");
    }
    @DeleteMapping("/api/groups/{groupId}/users/{userId}/remove")
    public ResponseEntity<?> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) throws Exception {
        groupService.RemoveUserFromGroup(userId, groupId);
        return ResponseEntity.ok("User removed from group.");
    }



    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable Long id) throws IOException {
       byte[] excelData= groupService.exportToExcel(id);

       HttpHeaders headers=new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "group_" + id + ".xlsx");

        return new ResponseEntity<>(excelData,headers,HttpStatus.OK);
    }
}