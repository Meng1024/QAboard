package com.QAboard.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QAboard.model.Comment;
import com.QAboard.model.EntityType;
import com.QAboard.model.HostHolder;
import com.QAboard.model.Question;
import com.QAboard.model.ViewObject;
import com.QAboard.service.CommentService;
import com.QAboard.service.LikeService;
import com.QAboard.service.QuestionService;
import com.QAboard.service.UserService;
import com.QAboard.util.QAboardUtil;



@Controller
public class QuestionController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;
    
    @Autowired
    HostHolder hostHolder;
    
    @Autowired
    CommentService commentService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    LikeService likeService;
    
    /*@RequestMapping(path = {"/question/{qid}"}, method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        
    }*/
    
    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            if(hostHolder.getUser() == null) {
                question.setUserId(QAboardUtil.ANONYMOUS_USERID);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (questionService.addQuestion(question) > 0) {
                return QAboardUtil.getJSONString(0);
            }
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("Adding question failure.");
        }
        return QAboardUtil.getJSONString(1, "failure");
    }
    
    @RequestMapping(value = "question/{qid}", method = {RequestMethod.GET})
    public String detail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectByID(qid);
        model.addAttribute("question", question);

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<ViewObject>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }

        model.addAttribute("comments", comments);
        return "detail";
    }
}
