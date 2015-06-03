/* 
 * @(#)StudentService.java    Created on 2015-5-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.service;

import java.util.List;

import wanpeng.entity.Student;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-15 下午4:00:45 $
 */
public interface StudentService {
    public abstract List<Student> getStudent();
}
