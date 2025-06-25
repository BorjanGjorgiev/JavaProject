package com.example.intecproject.service.impl;
import com.example.intecproject.model.Group;
import com.example.intecproject.model.User;
import com.example.intecproject.repository.GroupRepository;
import com.example.intecproject.repository.UserRepository;
import com.example.intecproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public User update(Long id, User user) {
        User user1=findById(id);
        user1.setEmail(user.getEmail());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setAvailable(user.getAvailable());
        userRepository.save(user1);
        return user1;
    }
    @Override
    public void delete(Long id)
    {
        User user=findById(id);
        userRepository.delete(user);
    }
    @Override
    public List<User> importFromFile(MultipartFile file, User user) throws IOException {
        return null;
    }
    @Override
    public void PlaceUserInGroup(Long userId, Long groupId) {
        Group g=groupRepository.findById(groupId).orElse(null);
        User user=userRepository.findById(userId).orElse(null);
        user.setGroup(g);
        userRepository.save(user);
    }
    @Override
    public void RemoveUserFromGroup(Long userId, Long groupId)
    {
        User u=userRepository.findById(userId).orElse(null);
        Group g=groupRepository.findById(groupId).orElse(null);
        if(g.equals(u.getGroup()))
        {
            u.setGroup(null);
            userRepository.save(u);
        }
    }

    @Override
    public void ChangeAvailabilityOfUser(@PathVariable Long userId) {
        User u=userRepository.findById(userId).orElse(null);
        u.setAvailable(!u.getAvailable());

        userRepository.save(u);
    }

    @Override
    public List<User> filterByDateBefore(LocalDateTime date) {
        return userRepository.findAll().stream().filter(x->x.getCreatedAt().isBefore(date)).collect(Collectors.toList());
    }
    @Override
    public List<User> filterByDateAfter(LocalDateTime date) {
        return userRepository.findAll().stream().filter(x->x.getCreatedAt().isBefore(date)).collect(Collectors.toList());
    }



    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        userRepository.save(user);
    }
}
