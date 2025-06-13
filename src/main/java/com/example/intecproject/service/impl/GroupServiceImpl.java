package com.example.intecproject.service.impl;

import com.example.intecproject.exception.NoUsersInGroupException;
import com.example.intecproject.model.Group;
import com.example.intecproject.model.User;
import com.example.intecproject.repository.GroupRepository;
import com.example.intecproject.repository.UserRepository;
import com.example.intecproject.service.GroupService;

import java.util.List;

public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group saveGroup(Group g) {
        return groupRepository.save(g);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteGroup(Long Id) {

        Group g=findById(Id);

        groupRepository.delete(g);

    }

    @Override
    public void addUserToGroup(Long userId, Long groupId) throws Exception {
        Group g=findById(groupId);
        User u=userRepository.findById(userId).orElse(null);

        if(g!=null && u!=null)
        {
            if(!g.getUsers().isEmpty())
            {
                g.addUser(u);
                groupRepository.save(g);
            }
            else
            {
                throw new NoUsersInGroupException(groupId);
            }
        }
        else
        {
            throw new Exception("There are no groups and users with the provided ID");
        }
    }

    @Override
    public void RemoveUserFromGroup(Long userId, Long groupId) throws Exception {
        Group g=findById(groupId);
        User u=userRepository.findById(userId).orElse(null);


        if(g!=null && u!=null) {
            if (g.getUsers().contains(u)) {
                g.getUsers().remove(u);

                u.setGroup(null);


                groupRepository.save(g);
                userRepository.save(u);
            } else
            {
                throw new NoUsersInGroupException(groupId);

            }
        }
        else
        {
            throw new Exception("There are no groups and users with the provided ID");
        }
    }

    @Override
    public byte[] exportToPDF(String name) {
        return new byte[0];
    }
}
