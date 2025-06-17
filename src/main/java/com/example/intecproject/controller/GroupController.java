package com.example.intecproject.controller;


import com.example.intecproject.model.Group;
import org.springframework.ui.Model;
import com.example.intecproject.service.GroupService;
import com.example.intecproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
    public String listAllGroups(Model model)
    {
        model.addAttribute("groups",groupService.findAll());
        return "groups";
    }


    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id,Model model)
    {
        groupService.deleteGroup(id);
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String getDetailsForGroup(@PathVariable Long id,Model model)
    {
       Group g=groupService.findById(id);

       model.addAttribute("group",g);
        return "groups-detail";
    }











}
