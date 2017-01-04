package com.QAboard.util;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    
    //follower
    private static String BIZ_FOLLOWER = "FOLLOWER"; 
    
    //followee
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    
    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT +  String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    public static String getDislikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT +  String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }
    
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE  + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
}
