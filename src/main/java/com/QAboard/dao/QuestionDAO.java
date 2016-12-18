package com.QAboard.dao;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.QAboard.model.Question;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count "; 
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, 
        ") values (#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);
    
    List<Question> selectLastestQuestions(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit); 
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id=#{id}"})
    Question selectByID(int id);
    
}
