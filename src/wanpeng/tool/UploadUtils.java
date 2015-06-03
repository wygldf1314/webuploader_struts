/* 
 * @(#)UploadUtils.java    Created on 2015-3-31
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import wanpeng.entity.UploadFile;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-3-31 下午9:08:11 $
 */
public class UploadUtils {

    /**
     * @return
     * @throws Exception
     */
    public static UploadFile[] handleFileUpload() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, String[]> maps = request.getParameterMap();
        Enumeration<String> names = request.getParameterNames();
        System.out.println("------------------------------s");
        String key = null;
        String chunks = "";
        while (names.hasMoreElements()) {
            key = names.nextElement();
            if ("chunk".equals(key)) {
                chunks = maps.get(key)[0];
            }
            System.out.println(key + ":" + maps.get(key)[0]);
        }
        System.out.println("------------------------------e");
        if (request instanceof MultiPartRequestWrapper) {
            MultiPartRequestWrapper requestWrapper = (MultiPartRequestWrapper) request;
            if (!requestWrapper.hasErrors()) {
                Enumeration<String> e = requestWrapper.getFileParameterNames();
                List<UploadFile> filelist = new ArrayList<UploadFile>();
                while (e.hasMoreElements()) {
                    String fieldName = e.nextElement();
                    // uploaded file properties
                    File[] uploadedFiles = requestWrapper.getFiles(fieldName);
                    if ((uploadedFiles == null) || (uploadedFiles.length == 0)) {
                        Exception fileUploadException = new Exception("文件上传表单域读取错误: " + fieldName);
                        throw fileUploadException;
                    }
                    String[] uploadNames = requestWrapper.getFileNames(fieldName);
                    String[] contentTypes = requestWrapper.getContentTypes(fieldName);

                    if ((uploadNames.length == contentTypes.length) && (uploadNames.length == uploadedFiles.length)) {
                        String fileName;
                        for (int i = 0; i < uploadedFiles.length; i++) {
                            fileName = uploadNames[i];
                            int slash = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
                            if (slash > -1) {
                                fileName = fileName.substring(slash + 1);
                            }
                            UploadFile uploadfile = new UploadFile(fileName, uploadedFiles[i], contentTypes[i],
                                    fieldName, chunks);
                            filelist.add(uploadfile);
                        }
                    }
                }
                return filelist.toArray(new UploadFile[0]);
            }
        }
        return null;
    }
}
