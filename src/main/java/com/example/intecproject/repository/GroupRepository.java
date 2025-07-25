package com.example.intecproject.repository;

import com.example.intecproject.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
Group findByGroupName(String groupName);

}
