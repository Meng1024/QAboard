package com.QAboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QAboard.service.QAboardService;

@Controller
public class SettingController {
    @Autowired
    QAboardService qaService;
    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting() {
        return "hello setting  " + qaService.getMessage(0);
    }
    
}
