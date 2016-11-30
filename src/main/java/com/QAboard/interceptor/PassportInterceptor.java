package com.QAboard.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.QAboard.dao.LoginTicketDAO;
import com.QAboard.dao.UserDAO;
import com.QAboard.model.HostHolder;
import com.QAboard.model.LoginTicket;
import com.QAboard.model.User;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private HostHolder hostHolder;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("pre");
        String ticket = null;
        if(request.getCookies() != null) {
          for(Cookie cookie: request.getCookies()) {
              if(cookie.getName().equals("ticket")) {
                  ticket = cookie.getValue();
                  break;
              }
          }  
        } 
        if(ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId()); 
            hostHolder.setUser(user);
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("post");
        if(modelAndView != null && hostHolder.getUser() != null) {
             modelAndView.addObject("user", hostHolder.getUser());
        }
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        hostHolder.clear();
    }
    

}
