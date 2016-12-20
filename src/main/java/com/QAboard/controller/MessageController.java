package com.QAboard.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QAboard.model.HostHolder;
import com.QAboard.model.Message;
import com.QAboard.model.User;
import com.QAboard.model.ViewObject;
import com.QAboard.service.MessageService;
import com.QAboard.service.UserService;
import com.QAboard.util.QAboardUtil;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConvesationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
                model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("message fail" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            messageService.clearUnRead(hostHolder.getUser().getId(), conversationId);
        } catch (Exception e) {
            logger.error("message fail" + e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return QAboardUtil.getJSONString(999, "not log in");
            }
            User user = userService.selectByName(toName);
            if (user == null) {
                return QAboardUtil.getJSONString(1, "User doesnot exist");
            }

            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(hostHolder.getUser().getId());
            msg.setToId(user.getId());
            msg.setCreatedDate(new Date());
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.sendMessage(msg);
            return QAboardUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("sending message fail" + e.getMessage());
            return QAboardUtil.getJSONString(1, "sending message fail");
        }
    }


    @RequestMapping(path = {"/msg/jsonAddMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.sendMessage(msg);
            return QAboardUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("comment fail" + e.getMessage());
            return QAboardUtil.getJSONString(1, "adding comment fail");
        }
    }
}