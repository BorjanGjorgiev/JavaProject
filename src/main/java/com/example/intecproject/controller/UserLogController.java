package com.example.intecproject.controller;


import com.example.intecproject.model.UserLog;
import com.example.intecproject.repository.UserLogRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping("/api/logs")
public class UserLogController
{
private final UserLogRepository userLogRepository;

    public UserLogController(UserLogRepository userLogRepository) {
        this.userLogRepository = userLogRepository;
    }
    @GetMapping
    public List<UserLog>getAllLogs()
    {
        return userLogRepository.findAll(Sort.by(Sort.Direction.DESC,"timestamp"));
    }
}
