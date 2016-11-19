package com.QAboard.service;

import org.springframework.stereotype.Service;

@Service
public class QAboardService {
    public String getMessage(int userID) {
        return "message from service: " + userID;
    }
}
