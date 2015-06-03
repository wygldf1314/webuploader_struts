/* 
 * @(#)MD5Filter.java    Created on 2015-4-7
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-4-7 上午11:42:23 $
 */
public class MD5Filter {
    public static String getMd5ByFile(File file) {
        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            if (null != in) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void main(String[] args) {
        String v = MD5Filter.getMd5ByFile(new File("D:\\unit_import.xml"));
        System.out.println(v.toUpperCase());
    }
}
