/* 
 * @(#)SpringJDBCTest.java    Created on 2015-5-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wanpeng.entity.Student;
import wanpeng.service.StudentService;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-15 下午4:10:34 $
 */
public class SpringJDBCTest {
    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentService studentService = (StudentService) act.getBean("studentService");
        List<Student> students = studentService.getStudent();
        for (Student student : students) {
            System.out.println(student.getNo());
            System.out.println(student.getName());
        }
    }
}
