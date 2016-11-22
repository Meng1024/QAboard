package com.QAboard.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.dao.QuestionDAO;
import com.QAboard.model.Question;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLastestQuestions(userId, offset, limit);
    }
}
