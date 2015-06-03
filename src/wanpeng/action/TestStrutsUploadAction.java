/* 
 * @(#)TestStrutsUploadAction.java    Created on 2015-4-1
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.action;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-4-1 上午10:17:47 $
 */
public class TestStrutsUploadAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private File file;

    public void testUploadFile() {
        System.out.println(file.getName());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
