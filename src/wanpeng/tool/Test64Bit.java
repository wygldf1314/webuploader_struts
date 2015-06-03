/* 
 * @(#)Test64Bit.java    Created on 2015-4-1
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-4-1 下午3:55:24 $
 */
public class Test64Bit {
    public static void main(String[] args) {
        BASE64Encoder encoder = new BASE64Encoder();
        String imageURL = "d:\\1.jpg";
        try {
            StringBuilder pictureBuffer = new StringBuilder();
            InputStream input = new FileInputStream(new File(imageURL));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            for (int len = input.read(temp); len != -1; len = input.read(temp)) {
                out.write(temp, 0, len);
                pictureBuffer.append(encoder.encode(out.toByteArray()));
                out.reset();
            }

            out(pictureBuffer.toString());
            out("Encoding the picture Success");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param string
     */
    private static void out(Object o) {
        System.out.println(o.toString());
    }

}
