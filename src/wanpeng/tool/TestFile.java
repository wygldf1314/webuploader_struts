/* 
 * @(#)TestFile.java    Created on 2015-4-7
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-4-7 下午5:39:40 $
 */
public class TestFile {
    public static void main(String[] args) throws Exception {
        /*
         * 将一个文件分割为多个文件,然后再组合成一个文件 找到文件,读入一个1M的buffer中,然后写入一个Part文件中,循环此操作直到文件读取完毕
         */

        String sourceFilePath = "D:" + File.separator + "Code" + File.separator + "source" + File.separator
                + "QQ截图20150526154820.png";
        int partFileLength = 1024 * 1024;// 指定分割的子文件大小为1M
        splitFile(sourceFilePath, partFileLength);// 将文件分割
        combineFile("D:" + File.separator + "Code" + File.separator + "target");// 将分割后的文件合并
    }

    public static void combineFile(String directoryPath) throws Exception {
        Properties config = new Properties();
        InputStream ips = null;
        ips = new FileInputStream(new File(directoryPath + File.separator + "config.properties"));
        config.load(ips);

        Set keySet = config.keySet();// 需要将keySet转换为int型

        // 将keySet迭代出来,转换成int类型的set,排序后存储进去
        Set<Integer> intSet = new TreeSet<Integer>();
        Iterator iterString = keySet.iterator();
        while (iterString.hasNext()) {
            String tempKey = (String) iterString.next();
            if ("name".equals(tempKey)) {
            }
            else {
                int tempInt;
                tempInt = Integer.parseInt(tempKey);
                intSet.add(tempInt);
            }
        }

        Set<Integer> sortedKeySet = new TreeSet<Integer>();
        sortedKeySet.addAll(intSet);

        OutputStream eachFileOutput = null;
        eachFileOutput = new FileOutputStream(new File("D:" + File.separator + "Code" + File.separator
                + config.getProperty("name")));

        Iterator iter = sortedKeySet.iterator();
        while (iter.hasNext()) {
            String key = new String("" + iter.next());
            if (key.equals("name")) {
            }
            else {
                // System.out.println("debug---");
                String fileNumber = null;
                String filePath = null;
                fileNumber = key;
                filePath = config.getProperty(fileNumber);

                // 循环读取文件 --> 依次写入

                InputStream eachFileInput = null;

                eachFileInput = new FileInputStream(new File(filePath));

                byte[] buffer = new byte[1024 * 1024 * 1];// 缓冲区文件大小为1M
                int len = 0;
                while ((len = eachFileInput.read(buffer, 0, 1024 * 1024 * 1)) != -1) {
                    eachFileOutput.write(buffer, 0, len);
                }
                eachFileInput.close();
            }
        }

        eachFileOutput.close();
    }

    public static void splitFile(String sourceFilePath, int partFileLength) throws Exception {
        File sourceFile = null;
        File targetFile = null;
        InputStream ips = null;
        OutputStream ops = null;
        OutputStream configOps = null;// 该文件流用于存储文件分割后的相关信息，包括分割后的每个子文件的编号和路径,以及未分割前文件名
        Properties partInfo = null;// properties用于存储文件分割的信息
        byte[] buffer = null;
        int partNumber = 1;
        sourceFile = new File(sourceFilePath);// 待分割文件
        ips = new FileInputStream(sourceFile);// 找到读取源文件并获取输入流
        configOps = new FileOutputStream(new File("D:" + File.separator + "Code" // 配置文件
                + File.separator + "target" + File.separator + "config.properties"));
        buffer = new byte[partFileLength];// 开辟缓存空间
        int tempLength = 0;
        partInfo = new Properties();// key:1开始自动编号 value:文件路径

        while ((tempLength = ips.read(buffer, 0, partFileLength)) != -1) {
            String targetFilePath = "D:" + File.separator + "Code" + File.separator + "target" + File.separator
                    + "part_" + (partNumber);// 分割后的文件路径+文件名
            partInfo.setProperty((partNumber++) + "", targetFilePath);// 将相关信息存储进properties
            targetFile = new File(targetFilePath);
            ops = new FileOutputStream(targetFile);// 分割后文件
            ops.write(buffer, 0, tempLength);// 将信息写入碎片文件

            ops.close();// 关闭碎片文件
        }
        partInfo.setProperty("name", sourceFile.getName());// 存储源文件名
        partInfo.store(configOps, "sdfetgd");// 将properties存储进实体文件中
        ips.close();// 关闭源文件流
    }
}
