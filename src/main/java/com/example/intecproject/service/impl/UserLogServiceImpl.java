package com.example.intecproject.service.impl;

import com.example.intecproject.model.UserLog;
import com.example.intecproject.repository.UserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserLogServiceImpl {

    @Autowired
    private UserLogRepository userLogRepository;

    public void log(String username, String action, String ipAddress) {
        UserLog log = new UserLog();
        log.setUsername(username);
        log.setAction(action);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());

        userLogRepository.save(log);
    }
}
