package com.QAboard.dao;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.QAboard.model.Question;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count "; 
    
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, 
        ") values (#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);
    
    List<Question> selectLastestQuestions(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit); 
}
