package com.QAboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.util.JedisAdapter;
import com.QAboard.util.RedisKeyUtil;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    
    public long getLikeCount(int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        return jedisAdapter.scard(likeKey);
    }
    
    public int getLikeStatus(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        } 
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        return jedisAdapter.sismember(dislikeKey, String.valueOf(userId)) ? -1 : 0;
    }
    
    public long like(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        
        
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    } 
    
    public long dislike(int userId, int entityId, int entityType) {
        String dislikeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));
        
        
        String likeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    } 
}
