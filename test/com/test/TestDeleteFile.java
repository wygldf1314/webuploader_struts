/* 
 * @(#)TestDeleteFile.java    Created on 2015-5-28
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.test;

import java.io.File;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-5-28 下午4:35:39 $
 */
public class TestDeleteFile {
    public static void main(String[] args) {
        File file = new File("D:\\Code\\source\\");
        deleteFile(file);
    }

    public static boolean deleteFile(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFile(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
