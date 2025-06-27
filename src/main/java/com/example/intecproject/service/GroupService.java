package com.example.intecproject.service;

import com.example.intecproject.exception.NoUsersInGroupException;
import com.example.intecproject.model.Group;

import java.io.IOException;
import java.util.List;

public interface GroupService {
    List<Group> findAll();
    Group saveGroup(Group g);

    Group findById(Long id);
    void deleteGroup(Long id);

    Group createGroup(String groupName);
    void addUserToGroup(Long userId,Long groupId) throws Exception, NoUsersInGroupException;
    void RemoveUserFromGroup(Long userId,Long groupId) throws Exception;
    byte[] exportToExcel(Long groupId) throws IOException;
}
