package com.example.intecproject.controller;


import com.example.intecproject.model.Group;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.intecproject.service.GroupService;
import com.example.intecproject.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/gpoups")
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

    @GetMapping("/{id}")
    public ResponseEntity<Group> getDetailsForGroup(@PathVariable Long id)
    {
        return ResponseEntity.ok(groupService.findById(id));
    }


}
