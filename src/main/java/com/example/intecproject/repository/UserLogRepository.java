package com.example.intecproject.repository;

import com.example.intecproject.model.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog,Long>
{
}
