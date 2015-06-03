/* 
 * @(#)TestJava.java    Created on 2015-5-27
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-27 下午2:30:11 $
 */
public class TestJava {

    /**
     * 测试Math.ceil这个函数的使用
     */
    @Test
    public void testMath() {
        System.out.println(Math.ceil(4.89));
    }

    /**
     * 测试文件的大小计算
     */
    @Test
    public void testFile() {
        File file = new File("F:\\redis\\redis-2.8.zip");
        System.out.println(file.getName());
        System.out.println(file.length());
    }

    /**
     * 测试set中treeset是不是有自动排序功能
     */
    @Test
    public void testSet() {
        Set<String> strSet = new TreeSet<String>();
        strSet.add("upload_cf188a01_3759_4f99_aa24_dd9480e1ebc3_00000044");
        strSet.add("upload_cf188a01_3759_4f99_aa24_dd9480e1ebc3_00000026");
        strSet.add("upload_cf188a01_3759_4f99_aa24_dd9480e1ebc3_00000053");
        strSet.add("upload_cf188a01_3759_4f99_aa24_dd9480e1ebc3_00000035");
        for (String str : strSet) {
            System.out.println(str);
        }
    }

    /**
     * 测试删除文件
     */
    @Test
    public void testFileDelete() {
        File file = new File("D:\\Code\\source\\abcd\\abc.txt");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 测试创建文件
     * 
     * @throws IOException
     */
    @Test
    public void testCreateFile() throws IOException {
        File file = new File("D:\\Code\\source\\1.jpg");
        InputStream in = new FileInputStream(file);
        File _file = new File("D:\\Code\\source\\efg");
        if (!_file.exists()) {
            _file.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(_file.getAbsolutePath() + "\\4.jpg");
        byte buffer[] = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
    }
}
