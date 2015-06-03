/* 
 * @(#)MySpringTest.java    Created on 2015-5-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-15 下午3:04:46 $
 */
public class MySpringTest {
    public static void main(String args[]) throws Exception {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        DataSource dataSource = (DataSource) ctx.getBean("dataSource", DataSource.class);

        String sql = "select * from student";

        Connection connection = dataSource.getConnection();

        Statement stm = connection.createStatement();

        ResultSet rs = stm.executeQuery(sql);

        while (rs.next())

        {

            System.out.println("用户名为:" + rs.getString(2));

        }

    }
}
