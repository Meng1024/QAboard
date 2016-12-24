package com.QAboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QAboard.dao.CommentDAO;
import com.QAboard.model.Comment;

@Service
public class CommentService {

//    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, 
//    ") values (#{content}, #{user_id}, #{entry_id}, #{entry_type}, #{created_Date}, #{status})"})
//int addComments(Comment comment);
//
//@Update({"update ", TABLE_NAME, " set status={status} where entity_id=#{entityId} and entity_type=#{entityType}"})
//void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType);
//
//@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, 
//    "where entity_type=#{entityType} and entity_id=#{entityId}"})
//Comment selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);
//
//@Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
//int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
//
    @Autowired
    CommentDAO commentDAO;
    
    @Autowired
    SensitiveService sensitiveService;
    
    public void addComment(Comment comment) {
        comment.setContent(sensitiveService.filter(comment.getContent()));
        commentDAO.addComments(comment);
    }
    
    public void deleteComment(int entityId, int entityType, int status) {
        commentDAO.updateStatus(entityId, entityType, status);
    }
    
    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }
    
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }
    
    public Comment getCommentById(int commentId) {
        return commentDAO.getCommentById(commentId);
    } 
}
