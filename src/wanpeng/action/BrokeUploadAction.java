/* 
 * @(#)BrokeUploadAction.java    Created on 2015-6-16
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import wanpeng.entity.UploadFile;
import wanpeng.tool.UploadUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-6-16 上午10:37:12 $
 */
public class BrokeUploadAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private String md5;
    private String name;
    private String chunkIndex;
    private String md5Mark;
    private String fileName;

    /**
     * 这个上传文件的方法是按照 先把文件分成多少块，然后把每一块文件的名字和路径和顺序存放在一个property文件中
     * 
     */
    public void brokeUpload() {
        UploadFile file = null;
        try {
            UploadFile[] files = UploadUtils.handleFileUpload();// 获取的文件
            // System.out.println("rrrrr--" + files[0].getFileSize());
            OutputStream configOps = null;// 写入文件流
            Properties partInfo = null;// 创建property
            partInfo = new Properties();
            // 把分块的文件key和value写入到property文件中
            for (int i = 0; i < files.length; i++) {
                Properties config = new Properties();
                InputStream ips = null;
                file = files[i];
                String path = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + "property";
                File propFile = new File(path);// 配置文件
                File tempFile = file.getFile();// 构造临时对象
                String uploadPath = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator
                        + "code";
                // 把临时路径的文件信息写到指定的路径下面
                InputStream in = new FileInputStream(tempFile);
                File _file = new File(uploadPath);
                if (!_file.exists()) {
                    _file.mkdirs();
                }
                FileOutputStream out = new FileOutputStream(_file.getAbsolutePath() + File.separator
                        + tempFile.getName());
                byte buffer[] = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();
                in.close();
                // 路径是否存在
                if (!propFile.exists()) {
                    propFile.mkdirs();
                    configOps = new FileOutputStream(propFile.getAbsolutePath() + File.separator + "config.properties");
                    partInfo.setProperty("name", file.getFileName());// 存储源文件名
                    String targetFilePath = _file.getAbsolutePath() + File.separator + tempFile.getName();
                    partInfo.setProperty(file.getChunks(), targetFilePath);
                }
                else {
                    ips = new FileInputStream(propFile.getAbsolutePath() + File.separator + "config.properties");
                    config.load(ips);
                    Set<Object> keySet = config.keySet();
                    Iterator<Object> iterString = keySet.iterator();
                    while (iterString.hasNext()) {
                        String key = (String) iterString.next();
                        String value = config.getProperty(key);
                        partInfo.setProperty(key, value);
                    }
                    configOps = new FileOutputStream(propFile.getAbsolutePath() + File.separator + "config.properties");
                    String targetFilePath = _file.getAbsolutePath() + File.separator + tempFile.getName();
                    partInfo.setProperty("name", file.getFileName());
                    partInfo.setProperty(file.getChunks(), targetFilePath);
                    ips.close();
                }
            }
            partInfo.store(configOps, "sdfetgd");
            configOps.close();
        }
        catch (Exception e) {
            addActionError("文件上传失败");
        }
    }

    /**
     * 根据property中的key和value对文件进行合并
     * 
     * @throws IOException
     */
    public void mergeImage() throws IOException {
        String path = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + "property"
                + File.separator + "config.properties";
        Properties config = new Properties();
        InputStream ips = null;
        ips = new FileInputStream(new File(path));
        config.load(ips);
        Set<Object> keySet = config.keySet();
        Set<Integer> intSet = new TreeSet<Integer>();
        Iterator<Object> iterString = keySet.iterator();
        while (iterString.hasNext()) {
            String tempKey = (String) iterString.next();
            if (!"name".equals(tempKey)) {
                int tempInt;
                tempInt = Integer.parseInt(tempKey);
                intSet.add(tempInt);
            }
        }
        Set<Integer> sortedKeySet = new TreeSet<Integer>();
        sortedKeySet.addAll(intSet);

        OutputStream eachFileOutput = null;
        eachFileOutput = new FileOutputStream(new File("F:" + File.separator + "temp1" + File.separator + "target"
                + File.separator + config.getProperty("name")));
        Iterator<Integer> iter = sortedKeySet.iterator();
        while (iter.hasNext()) {
            String key = new String("" + iter.next());
            if (!"name".equals(key)) {
                String fileNumber = null;
                String filePath = null;
                fileNumber = key;
                filePath = config.getProperty(fileNumber);

                InputStream eachFileInput = null;
                eachFileInput = new FileInputStream(new File(filePath));
                byte[] buffer = new byte[1024 * 1024 * 1];
                int len = 0;
                while ((len = eachFileInput.read(buffer, 0, 1024 * 1024 * 1)) != -1) {
                    eachFileOutput.write(buffer, 0, len);
                }
                eachFileInput.close();
            }
        }
        eachFileOutput.close();
        ips.close();
        System.out.println("uploadImage success");
    }

    /**
     * 删除文件信息
     * 
     * @return
     */
    public boolean deleteFile(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFile(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 检查文件的md5，看看是否和上传路径上面的文件md5值相等
     * 
     * @return
     * @throws Exception
     */
    public void checkMD5() throws Exception {
        if (StringUtils.isNotEmpty(md5)) {
            // 文件夹的路径
            String path = "F:" + "/" + "temp1" + "/" + "target" + "/" + md5;
            File file = new File(path);
            String[] chunkFileNames = null;
            // 文件的路径
            String filePath = "F:" + "/" + "temp1" + "/" + "target" + "/" + fileName;
            String md5Value = null;
            ServletActionContext.getResponse().setContentType("text/html; charset=utf-8");
            PrintWriter print = ServletActionContext.getResponse().getWriter();
            if (new File(filePath).exists()) {
                FileInputStream fis = new FileInputStream(filePath);
                md5Value = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
                IOUtils.closeQuietly(fis);
                if (md5.equals(md5Value)) {
                    print.write("{\"ifExist\": 1, \"path\": \"" + filePath + "\"}");
                }
            }

            if (file.exists()) {
                File[] files = file.listFiles();
                chunkFileNames = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    chunkFileNames[i] = files[i].getName();
                }
                print.write("{\"chunkFileNames\":\"" + chunkFileNames + "\"}");
            }
            print.close();
            System.out.println("MD5:" + md5Value);
            System.out.println("web uploaderMD5:" + md5);

        }
    }

    /**
     * 上传每一块文件之前检查这个文件
     */
    public void checkCheck() {
        try {
            UploadFile[] files = UploadUtils.handleFileUpload();// 获取的文件
            File file = files[0].getFile();
            System.out.println(file.getAbsolutePath());
        }
        catch (Exception e) {
            addActionError("文件上传失败");
        }
    }

    /**
     * 点击上传这个链接
     */
    public void uploadFile() {
        System.out.println(123);
        System.out.println("+++++++++" + md5);
    }

    /**
     * 根据md5的值创建文件加并存放文件
     */
    public void uploadFileByMd5() {
        UploadFile file = null;
        try {
            UploadFile[] files = UploadUtils.handleFileUpload();// 获取的文件
            // 把分块的文件key和value写入到property文件中
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                File tempFile = file.getFile();// 构造临时对象
                String uploadPath = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + md5;
                // 把临时路径的文件信息写到指定的路径下面
                InputStream in = new FileInputStream(tempFile);
                File _file = new File(uploadPath);
                if (!_file.exists()) {
                    _file.mkdirs();
                }

                String tempName = tempFile.getName();// 临时文件的名字
                FileOutputStream out = new FileOutputStream(_file.getAbsolutePath() + File.separator + file.getChunks()
                        + tempName.substring(tempName.lastIndexOf(".")));
                byte buffer[] = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();
                in.close();
            }
        }
        catch (Exception e) {
            addActionError("文件上传失败");
        }
    }

    /**
     * 根据md5的值合并文件
     * 
     * @throws IOException
     */
    public void mergeFileByMd5() throws IOException {
        String uploadPath = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + md5;
        File file = new File(uploadPath);
        File[] files = file.listFiles();
        OutputStream eachFileOutput = null;
        eachFileOutput = new FileOutputStream(new File("F:" + File.separator + "temp1" + File.separator + "target"
                + File.separator + fileName));
        for (File _file : files) {
            String fileName = _file.getAbsolutePath();
            InputStream eachFileInput = null;
            eachFileInput = new FileInputStream(new File(fileName));
            byte[] buffer = new byte[1024 * 1024 * 1];
            int len = 0;
            while ((len = eachFileInput.read(buffer, 0, 1024 * 1024 * 1)) != -1) {
                eachFileOutput.write(buffer, 0, len);
            }
            eachFileInput.close();
        }
        eachFileOutput.close();
        deleteFile(file);
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(String chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public String getMd5Mark() {
        return md5Mark;
    }

    public void setMd5Mark(String md5Mark) {
        this.md5Mark = md5Mark;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
