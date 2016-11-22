package com.QAboard;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.QAboard.dao.QuestionDAO;
import com.QAboard.dao.UserDAO;
import com.QAboard.model.Question;
import com.QAboard.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QAboardApplication.class)
@Sql("/init-schema.sql")
public class initDatabaseTest {
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    QuestionDAO questionDAO;
    
    @Test
    public void initTest() {
       Random random = new Random();
       for(int i = 0; i < 10; i++) {
           User user = new User();
           user.setId(0);
           user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
           user.setName(String.format("USER%d", i));
           user.setPassword("cc");
           user.setSalt("bb");
           
           userDAO.addUser(user);
           
           
           Question question = new Question();
           question.setCommentCount(i);
           Date date = new Date();
           date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
           question.setCreatedDate(date);
           question.setUserId(i + 1);
           question.setTitle(String.format("TITLE{%d}", i));
           question.setContent(String.format("cccccccccccccccccccccccccccccccccc%d", i));
           questionDAO.addQuestion(question);
       } 
       System.out.println("test output");
       System.out.println(questionDAO.selectLastestQuestions(4,0,5));
       
    }
    
    
    @Test
    public void test() {
      try {
      //加载MYSQL JDBC驱动程序   
      Class.forName("com.mysql.jdbc.Driver");     
      //Class.forName("org.gjt.mm.mysql.Driver");
      System.out.println("Success loading Mysql Driver!");
      }
      catch (Exception e) {
          System.out.print("Error loading Mysql Driver!");
          e.printStackTrace();
      }
      try {
          Connection connect = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/QAboard","root","abc123");
           //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

          System.out.println("Success connect Mysql server!");
          Statement stmt = connect.createStatement();
          ResultSet rs = stmt.executeQuery("select * from user");
          
          //user 为你表的名称
          while (rs.next()) {
            System.out.println(rs.getString("name"));
          }
      }
      catch (Exception e) {
          System.out.print("get data error!");
          e.printStackTrace();
      }
    }
}
