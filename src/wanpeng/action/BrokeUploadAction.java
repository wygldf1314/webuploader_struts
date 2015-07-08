/* 
 * @(#)BrokeUploadAction.java    Created on 2015-6-16
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
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
    private String uploadFileStatus;// 上传了哪几块文件

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
            // 存放分了多少块的文件的路径
            String path = "F:" + "/" + "temp1" + "/" + "target" + "/" + md5 + File.separator
                    + fileName.substring(0, fileName.lastIndexOf(".")) + ".txt";
            File file = new File(path);
            // 文件的路径
            String filePath = "F:" + "/" + "temp1" + "/" + "target" + "/" + md5 + "/" + fileName;
            String md5Value = null;
            ServletActionContext.getResponse().setContentType("text/html; charset=utf-8");
            PrintWriter print = ServletActionContext.getResponse().getWriter();
            if (new File(filePath).exists()) {
                MessageDigest MD5 = MessageDigest.getInstance("MD5");
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(new File(filePath));
                    byte[] buffer = new byte[1024 * 1024 * 1];
                    int length;
                    while ((length = fileInputStream.read(buffer)) != -1) {
                        MD5.update(buffer, 0, length);
                        break;
                    }
                    md5Value = new String(Hex.encodeHex(MD5.digest()));
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (md5.equals(md5Value)) {
                    print.write("{\"ifExist\": 1, \"path\": \"" + filePath + "\"}");
                }
            }

            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bf = new BufferedReader(fileReader);
                String _str;
                String str = "";
                while ((_str = bf.readLine()) != null) {
                    str = str + _str;
                }
                fileReader.close();
                bf.close();
                print.write("{\"chunkFileNames\":\"" + str + "\"}");
            }
            print.close();
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
                File formalFile = new File("F:" + File.separator + "temp1" + File.separator + "target" + File.separator
                        + md5 + File.separator + file.getFileName());
                if (!formalFile.exists()) {
                    formalFile.mkdir();
                }
                String logName = file.getFileName().substring(0, file.getFileName().lastIndexOf("."));
                File logFile = new File("F:" + File.separator + "temp1" + File.separator + "target" + File.separator
                        + md5);
                if (!logFile.exists()) {
                    logFile.mkdirs();
                }

                FileWriter fileWrite = new FileWriter(logFile.getAbsolutePath() + File.separator + logName + ".txt");
                fileWrite.write(file.getChunks());
                fileWrite.close();
                // 打开一个随机访问文件流，按读写方式
                RandomAccessFile randomFile = new RandomAccessFile(formalFile, "rw");
                // 文件长度，字节数
                long fileLength = randomFile.length();
                // 将写文件指针移到文件尾。
                randomFile.seek(fileLength);
                InputStream eachFileInput = new FileInputStream(tempFile);
                byte[] buffer = new byte[1024 * 1024 * 1];
                int len = 0;
                while ((len = eachFileInput.read(buffer, 0, 1024 * 1024 * 1)) != -1) {
                    randomFile.write(buffer, 0, len);
                }
                eachFileInput.close();
                randomFile.close();
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
        // 上传文件的临时路径
        String uploadPath = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + md5;
        File file = new File(uploadPath);
        File[] files = file.listFiles();
        OutputStream eachFileOutput = null;
        eachFileOutput = new FileOutputStream(new File("F:" + File.separator + "temp1" + File.separator + "target"
                + File.separator + fileName));
        // 合并临时路径下面的临时文件
        for (int i = 0; i < files.length; i++) {
            String fileName = uploadPath + File.separator + i + ".tmp";
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
        String tempFile = "F:" + "/" + "temp1" + "/" + "target" + "/" + fileName;
        ServletActionContext.getResponse().setContentType("text/html; charset=utf-8");
        PrintWriter print = ServletActionContext.getResponse().getWriter();
        print.write("{\"ifExist\": 1, \"path\": \"" + tempFile + "\"}");
    }

    /**
     * 删除临时文件
     */
    public void deleteTxtFile() {
        String textName = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + md5
                + File.separator + fileName.substring(0, fileName.lastIndexOf(".")) + ".txt";
        deleteFile(new File(textName));
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

    public String getUploadFileStatus() {
        return uploadFileStatus;
    }

    public void setUploadFileStatus(String uploadFileStatus) {
        this.uploadFileStatus = uploadFileStatus;
    }

}
