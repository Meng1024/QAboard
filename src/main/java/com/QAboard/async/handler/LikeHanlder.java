package com.QAboard.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QAboard.async.EventHandler;
import com.QAboard.async.EventModel;
import com.QAboard.async.EventType;
import com.QAboard.model.Message;
import com.QAboard.model.User;
import com.QAboard.service.MessageService;
import com.QAboard.service.UserService;
import com.QAboard.util.QAboardUtil;

@Component
public class LikeHanlder implements EventHandler {
    @Autowired
    MessageService messageService;
    
    @Autowired
    UserService userService;
    
    @Override
    public void doHandle(EventModel model) {
        
        System.out.println(model.getActorId()  + " " + model.getEntityOwnerId() + "******************");
        if(model.getActorId() != model.getEntityOwnerId()) {
            Message message = new Message();
            message.setFromId(QAboardUtil.SYSTEM_USERID);
            message.setToId(model.getEntityOwnerId());
            message.setCreatedDate(new Date());
            User user = userService.getUser(model.getActorId());
            message.setContent("User" + user.getName() + 
                    " like your comment.\n" + " http://127.0.0.1:8080/question/" + model.getExt("questionId"));
            messageService.sendMessage(message);
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }

}
