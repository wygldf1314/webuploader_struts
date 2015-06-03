/* 
 * @(#)StudentServiceImpl.java    Created on 2015-5-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import wanpeng.entity.Student;
import wanpeng.service.StudentService;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-15 下午4:03:26 $
 */
@SuppressWarnings("unchecked")
public class StudentServiceImpl implements StudentService {
    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Student> getStudent() {
        List<Student> list = template.query("select * from student", new RowMapper() {

            @Override
            public Student mapRow(ResultSet rs, int arg1) throws SQLException {
                Student student = new Student();
                student.setNo(rs.getString("no"));
                student.setName(rs.getString("name"));
                return student;
            }
        });
        return list;
    }

}
