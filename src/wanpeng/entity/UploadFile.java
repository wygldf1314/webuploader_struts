/* 
 * @(#)UploadFile.java    Created on 2015-3-31
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.entity;

import java.io.File;
import java.io.Serializable;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-3-31 下午7:57:51 $
 */
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private File tempFile;
    private String contentType;
    private String fieldName;
    private String chunks;

    public UploadFile(File tempFile) {
        this.tempFile = tempFile;
        this.fileName = tempFile.getName();
    }

    public UploadFile(File tempFile, String contentType) {
        this.tempFile = tempFile;
        this.fileName = tempFile.getName();
        this.contentType = contentType;
    }

    public UploadFile(String fileName, File tempFile, String contentType, String fieldName, String chunks) {
        this.fileName = fileName;
        this.tempFile = tempFile;
        this.contentType = (contentType == null) ? "" : contentType;
        this.fieldName = fieldName;
        this.chunks = chunks;
    }

    /**
     * @return Returns the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 文件大小
     * 
     * @return
     */
    public long getFileSize() {
        return tempFile.length();
    }

    /**
     * 文件类型
     * 
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 实际保存到硬盘上的文件
     * 
     * @return Returns the file.
     */
    public File getFile() {
        return tempFile;
    }

    /**
     * 实际的文件名 文件名
     * 
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return getFileName();
    }

    public String getChunks() {
        return chunks;
    }

    public void setChunks(String chunks) {
        this.chunks = chunks;
    }

}
