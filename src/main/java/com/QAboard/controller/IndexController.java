package com.QAboard.controller;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.QAboard.model.User;
import com.QAboard.service.QAboardService;

//@Controller
public class IndexController {
    @Autowired
    QAboardService qaService;
    
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    
    @ResponseBody
    public String index() {
        return qaService.getMessage(1) + " Hello World ";
    }
    
    
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId, 
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key) {
        return userId + " " + groupId + " " + type + " " + key;
    }
    
    @RequestMapping(path = {"/vm"})
    public String template(Model model) {
        model.addAttribute("value1", "hello");
        List<String> colors = Arrays.asList("r", "g", "b");
        model.addAttribute("colors", colors);
        
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("lee"));
        return "index";
            
    }
    
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse reponse, 
                           HttpServletRequest request, 
                           HttpSession httpSession,
                           @CookieValue("JSESSIONID") String sessionID) {
        //get headers
        Enumeration<String> headersName = request.getHeaderNames();
        while(headersName.hasMoreElements()) {
            System.out.println("name " + ": " + request.getHeader(headersName.nextElement()));
        }
        
        //get cookies
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                System.out.println("name: " + cookie.getName() + " value: " + cookie.getValue());
            }
        }
        System.out.println(sessionID);
        
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod() +  "<\br>");
        sb.append(request.getPathInfo() + "<\br>");
        return sb.toString();
        
    }
    
    
   @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
   public RedirectView redirect(@PathVariable("code") int code, HttpSession httpSession) {
       httpSession.setAttribute("msg", "from 301");
       RedirectView red = new RedirectView("/", true);
       if(code == 301) {
           red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
       }
       return red;
   }
   
   
   @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
   @ResponseBody
   public String admin(@RequestParam("key") String key) {
       if("admin".equals(key)) {
           return "hello admin";
       } else {
           throw new IllegalArgumentException("wrong argument");
       }
   }
   
   @ExceptionHandler()
   @ResponseBody
   public String error(Exception e) {
       return "error: " + e.getMessage();
   }
}
