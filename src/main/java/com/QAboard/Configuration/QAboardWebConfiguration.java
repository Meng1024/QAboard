package com.QAboard.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.QAboard.interceptor.LoginRequireInterceptor;
import com.QAboard.interceptor.PassportInterceptor;

@Component
public class QAboardWebConfiguration extends WebMvcConfigurerAdapter{    
    @Autowired
    PassportInterceptor passportInterceptor;
    
    @Autowired
    LoginRequireInterceptor loginRequiredInterceptor;
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {        
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
  