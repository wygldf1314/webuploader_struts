/* 
 * @(#)TestMysqlConnect.java    Created on 2015-5-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-15 下午2:43:13 $
 */
public class TestMysqlConnect {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn;
        conn = DriverManager.getConnection("jdbc:mysql://192.168.174.128:3306/test?"
                + "user=root&password=wu&useUnicode=true&characterEncoding=UTF8");
        if (null == conn) {
            System.out.println("Can not connect");
        }
        else {
            System.out.println("It connect");
        }

        Statement st = conn.createStatement();
        String sql = "select * from student";
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        conn.close();
    }
}
