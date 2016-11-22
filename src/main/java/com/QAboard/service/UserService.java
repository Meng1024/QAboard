package com.QAboard.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.dao.UserDAO;
import com.QAboard.model.User;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;
    
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
