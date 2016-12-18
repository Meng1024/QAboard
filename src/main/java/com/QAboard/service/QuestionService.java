package com.QAboard.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.QAboard.dao.QuestionDAO;
import com.QAboard.model.Question;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    
    @Autowired
    SensitiveService sensitiveService;
    
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLastestQuestions(userId, offset, limit);
    }
    
    public int addQuestion(Question question) {
        //add filter
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setContent(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0; 
    }
    
    public Question selectByID(int id) {
        return questionDAO.selectByID(id);
    }
}
