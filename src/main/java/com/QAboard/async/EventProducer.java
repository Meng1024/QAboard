package com.QAboard.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.util.JedisAdapter;
import com.QAboard.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
