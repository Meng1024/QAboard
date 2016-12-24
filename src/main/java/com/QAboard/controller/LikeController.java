package com.QAboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.QAboard.model.EntityType;
import com.QAboard.model.HostHolder;
import com.QAboard.service.LikeService;
import com.QAboard.util.QAboardUtil;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    
    @Autowired
    HostHolder hostHolder;
    
    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    public String like(@RequestParam("commentId") int commentId) {
        System.out.println("like hello");
        if(hostHolder.getUser() == null) {
            return QAboardUtil.getJSONString(999);
        } 
        
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return QAboardUtil.getJSONString(0, String.valueOf(likeCount));
    }
    
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    public String dislike(@RequestParam("commentId") int commentId) {
        if(hostHolder.getUser() == null) {
            return QAboardUtil.getJSONString(999);
        } 
        long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return QAboardUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
