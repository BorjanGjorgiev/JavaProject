package com.example.intecproject.controller;


import com.example.intecproject.model.DTO.UserDTO;
import com.example.intecproject.model.Group;
import com.example.intecproject.model.User;
import com.example.intecproject.service.GroupService;
import com.example.intecproject.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api")@CrossOrigin(origins = "*")
@RestController

public class UserController {
    private final UserService userService;
    private final GroupService groupService;
    public UserController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }
    @GetMapping("/users")
    public List<User> listAllUsers()
    {
      return  userService.findAll();
    }
    @PostMapping("/add-user")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        return ResponseEntity.ok(userService.save(user));
    }
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user)
    {
        UserDTO userDTO=UserDTO.fromUser(user);
        return ResponseEntity.ok(userDTO);
    }
    @GetMapping("/users/details/{id}")
    public ResponseEntity<User> getDetailsForUser(@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/api/users/{userId}/groups/{groupId}/place")
    public ResponseEntity<?> placeUserInGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userService.PlaceUserInGroup(userId, groupId);
        return ResponseEntity.ok("User added to group.");
    }

    @PostMapping("/api/users/{userId}/groups/{groupId}/remove")
    public ResponseEntity<?> removeUserFromGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userService.RemoveUserFromGroup(userId, groupId);
        return ResponseEntity.ok("User removed from group.");
    }


    @PostMapping("/api/users/changeAvailability/{userId}")
    public ResponseEntity<Void> changeAvailability(@PathVariable Long userId)
    {
        userService.ChangeAvailabilityOfUser(userId);
        return ResponseEntity.noContent().build();
    }

}
