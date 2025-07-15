package com.example.intecproject.service;

import com.example.intecproject.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    User findById(Long id);

    User save(User user);
    User update(Long id,User user);

    void delete(Long id);

    List<User> importFromFile(MultipartFile file,User user) throws IOException;

    void PlaceUserInGroup(Long userId,Long groupId);

    void RemoveUserFromGroup(Long userId,Long groupId);

    void ChangeAvailabilityOfUser(Long userId);

    List<User>filterByDateBefore(LocalDateTime date);

    List<User> filterByDateAfter(LocalDateTime date);


    Optional<User> findByEmail(String email);

    void changePassword(Long id,String oldPassword,String newPassword);



}
