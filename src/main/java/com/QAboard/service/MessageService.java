package com.QAboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.dao.MessageDAO;
import com.QAboard.model.Message;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public void sendMessage(Message message) {
        messageDAO.sendMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConvesationUnreadCount(userId, conversationId);
    }

    public void clearUnRead(int userId, String conversationId) {
        messageDAO.clearUnRead(userId, conversationId);
    }
}
