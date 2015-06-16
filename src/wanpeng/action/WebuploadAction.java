/* 
 * @(#)WebuploadAction.java    Created on 2015-3-31
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Encoder;
import wanpeng.entity.UploadFile;
import wanpeng.tool.UploadUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-4-3 下午3:51:14 $
 */
public class WebuploadAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private File uploadFile; // 得到上传的文件,此属性对应于表单中文件字段的名称
    // 下面的这两个属性的命名必须遵守上定的规则，即为"表单中文件字段的名称" + "相应的后缀"
    private String uploadFileContentType; // 得到上传的文件的数据类型,
    private String uploadFileFileName; // 得到上传的文件的名称
    private String src;
    private String temPaths;
    private String md5;
    private List<File> _files = new ArrayList<File>();

    public void fileUpload() throws IOException {
        UploadFile file = null;
        try {
            UploadFile[] files = UploadUtils.handleFileUpload();
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                System.out.println(file.getFileName());
                File tempFile = file.getFile();// 构造临时对象
                System.out.println(tempFile.getAbsolutePath());
                System.out.println(tempFile.getParentFile());
                System.out.println(tempFile.getName());
                File _file = new File("F://temp1/webuploader/", file.getFileName());
                new FileOutputStream(_file);
                _files.add(_file);
            }
            if (files != null && files.length > 0) {
                // file = files[0];
                // String tempFile = file.getFile().getAbsolutePath();
                // writer("success" + tempFile);

            }
            System.out.println("The file size is " + _files.size());
        }
        catch (Exception e) {
            addActionError("文件上传失败");
        }
    }

    // public void mergerFile() throws Exception {
    // HttpServletRequest request = ServletActionContext.getRequest();
    // if (request instanceof MultiPartRequestWrapper) {
    // MultiPartRequestWrapper requestWrapper = (MultiPartRequestWrapper) request;
    // if (!requestWrapper.hasErrors()) {
    // Enumeration<String> e = requestWrapper.getFileParameterNames();
    // List<UploadFile> filelist = new ArrayList<UploadFile>();
    // while (e.hasMoreElements()) {
    // String fieldName = e.nextElement();
    // // uploaded file properties
    // File[] uploadedFiles = requestWrapper.getFiles(fieldName);
    // if ((uploadedFiles == null) || (uploadedFiles.length == 0)) {
    // Exception fileUploadException = new Exception("文件上传表单域读取错误: " + fieldName);
    // throw fileUploadException;
    // }
    // String[] uploadNames = requestWrapper.getFileNames(fieldName);
    // String[] contentTypes = requestWrapper.getContentTypes(fieldName);
    // if ((uploadNames.length == contentTypes.length) && (uploadNames.length == uploadedFiles.length)) {
    // String fileName;
    // for (int i = 0; i < uploadedFiles.length; i++) {
    // fileName = uploadNames[i];
    // int slash = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
    // if (slash > -1) {
    // fileName = fileName.substring(slash + 1);
    // }
    // UploadFile uploadfile = new UploadFile(fileName, uploadedFiles[i], contentTypes[i],
    // fieldName);
    // filelist.add(uploadfile);
    // }
    // }
    // }
    // }
    // }
    // }

    public void parseFile() {

    }

    public void showPath() {
        System.out.println(temPaths);
    }

    public void encodBase64() {
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            StringBuilder pictureBuffer = new StringBuilder();
            InputStream input = new FileInputStream(new File(src));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            for (int len = input.read(temp); len != -1; len = input.read(temp)) {
                out.write(temp, 0, len);
                pictureBuffer.append(encoder.encode(out.toByteArray()));
                out.reset();
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 往客户端浏览器写入信息
     * 
     * @param str
     * @throws IOException
     */
    public static void writer(String str) throws IOException {
        ServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding(ServletActionContext.getRequest().getCharacterEncoding());
        Writer writer = response.getWriter();
        try {
            writer.write(str);
        }
        finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * cropper对应的方法
     * 
     * @return
     */
    public void testCropper() {
        UploadFile file = null;
        try {
            UploadFile[] files = UploadUtils.handleFileUpload();
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                System.out.println(file.getFileName());
                File tempFile = file.getFile();// 构造临时对象
                System.out.println(tempFile.getAbsolutePath());
                System.out.println(tempFile.getParentFile());
                System.out.println(tempFile.getName());
                InputStream in = new FileInputStream(tempFile);
                File _file = new File("F://temp1/webuploader/" + file.getFileName());
                FileOutputStream out = new FileOutputStream(_file);
                byte buffer[] = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                _files.add(_file);
            }
            System.out.println("The file size is " + _files.size());
        }
        catch (Exception e) {
            addActionError("文件上传失败");
        }
    }

    /**
     * imageupload对应的方法
     * 
     * @return
     */
    public void testImageUpload() {
        UploadFile file = null;
        try {
            UploadFile[] files = UploadUtils.handleFileUpload();// 获取的文件
            System.out.println("rrrrr--" + files[0].getFileSize());
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
     * 合并上传的文件
     * 
     * @throws IOException
     */
    public void uploadImage() throws IOException {
        String path = "F:" + File.separator + "temp1" + File.separator + "target" + File.separator + "property"
                + File.separator + "config.properties";
        Properties config = new Properties();
        InputStream ips = null;
        ips = new FileInputStream(new File(path));
        config.load(ips);
        Set keySet = config.keySet();
        Set<Integer> intSet = new TreeSet<Integer>();
        Iterator iterString = keySet.iterator();
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
        Iterator iter = sortedKeySet.iterator();
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
     * 处理分块的文件
     */
    public void chunkUpload(HttpServletRequest request, HttpServletResponse response) {
        String chunkFileName;
        String fileMd5 = request.getParameter("fileMd5");
        Long fileSize = Long.parseLong(request.getParameter("size"));
        String fileName = request.getParameter("name");
        Integer fileType = Integer.parseInt(request.getParameter("fileType"));
        Integer isShared = Integer.parseInt(request.getParameter("isShared"));
        Integer chunks = 0, chunk = 0;
        String chunkStr = request.getParameter("chunk");
        String chunksStr = request.getParameter("chunks");
        String chunkMd5 = request.getParameter("chunkMd5");
        if (StringUtils.isEmpty(fileMd5)) {
            chunkFileName = fileName + fileSize;
        }
        else {
            chunkFileName = fileMd5;
        }
    }

    /************************* getter setter method **************************/
    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTemPaths() {
        return temPaths;
    }

    public void setTemPaths(String temPaths) {
        this.temPaths = temPaths;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public List<File> get_files() {
        return _files;
    }

    public void set_files(List<File> _files) {
        this._files = _files;
    }

}
