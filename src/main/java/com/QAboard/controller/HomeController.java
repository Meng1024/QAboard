package com.QAboard.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.QAboard.model.Question;
import com.QAboard.model.ViewObject;
import com.QAboard.service.QuestionService;
import com.QAboard.service.UserService;

@Controller
public class HomeController {
    @Autowired
    QuestionService questionSerivce;
    
    @Autowired
    UserService userService;
    
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET})
    public String template(Model model, @PathVariable int userId) {
        model.addAttribute("vos", getPairs(userId, 0, 10));
        return "index";
    }
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model) {
      
        model.addAttribute("vos", getPairs(0, 0, 10));
        return "index";
    }
    
    private List<ViewObject> getPairs(int userId, int offset, int limit) {
        List<Question> questionList = questionSerivce.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Question question: questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    } 
}

