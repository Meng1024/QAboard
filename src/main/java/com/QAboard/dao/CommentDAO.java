package com.QAboard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.QAboard.model.Comment;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status "; 
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

 
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, 
        ") values (#{content}, #{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status})"})
    int addComments(Comment comment);
    
    @Update({"update ", TABLE_NAME, " set status={status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, 
        "where entity_type=#{entityType} and entity_id=#{entityId}"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);
    
    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
