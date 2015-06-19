/* 
 * @(#)TestMD5.java    Created on 2015-6-18
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-6-18 下午7:08:49 $
 */
public class TestMD5 {
    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != in) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static void main(String[] args) throws IOException {

        String path = "F:\\temp1\\target\\code\\upload_3b42f40b_287c_4051_88fb_8155b46e0c7b_00000268.tmp";

        String v = getMd5ByFile(new File(path));
        System.out.println("MD5:" + v.toUpperCase());

        FileInputStream fis = new FileInputStream(path);
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
        IOUtils.closeQuietly(fis);
        System.out.println("MD5:" + md5);

        // System.out.println("MD5:"+DigestUtils.md5Hex("WANGQIUYUN"));
    }
}
