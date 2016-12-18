package com.QAboard.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.QAboard.model.Comment;
import com.QAboard.model.EntityType;
import com.QAboard.model.HostHolder;
import com.QAboard.service.CommentService;

@Controller
public class CommentController {
    public static final Logger logger = LoggerFactory.getLogger(CommentController.class);
   
    
    @Autowired
    HostHolder hostHolder;
    
    @Autowired
    CommentService commentService;
    
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        
     
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
       
     
        return "redirect:/question/" + String.valueOf(questionId);
    }
}
