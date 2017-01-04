package com.QAboard.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.util.JedisAdapter;
import com.QAboard.util.RedisKeyUtil;

@Service
public class FollwService {
    @Autowired
    JedisAdapter jedisAdapter;
    
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        jedis.mu
        return true;
    }
}
