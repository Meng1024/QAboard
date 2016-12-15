package com.QAboard.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QAboard.model.HostHolder;
import com.QAboard.model.Question;
import com.QAboard.service.QuestionService;
import com.QAboard.util.QAboardUtil;



@Controller
public class QuestionController {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;
    
    @Autowired
    HostHolder hostHolder;
    
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
}
