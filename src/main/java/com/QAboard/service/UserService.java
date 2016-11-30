package com.QAboard.service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.QAboard.dao.LoginTicketDAO;
import com.QAboard.dao.UserDAO;
import com.QAboard.model.LoginTicket;
import com.QAboard.model.User;
import com.QAboard.util.QAboardUtil;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isEmpty(username)) {
            map.put("msg", "The username cannot be empty");
            return map;
        }
        if(StringUtils.isEmpty(password)) {
            map.put("msg", "The password cannot be empty");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null) {
            map.put("msg", "The username already existed, please try another one");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(QAboardUtil.MD5(password + user.getSalt())); 
        userDAO.addUser(user); 
        
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isEmpty(username)) {
            map.put("msg", "The username cannot be empty");
            return map;
        }
        
        if(StringUtils.isEmpty(password)) {
            map.put("msg", "The password cannot be empty");
            return map;
        }
        
        User user = userDAO.selectByName(username);
        if(user == null) {
            map.put("msg", "Username does not exist");
            return map;
        }
        
        if(!QAboardUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "Wrong password or username");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
    
    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime() + 3600*24*100);
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();

    }
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
    
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
