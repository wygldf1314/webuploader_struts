/* 
 * @(#)MulThreadDownload.java    Created on 2015-6-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.tool;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-6-15 上午10:29:59 $
 */
public class MulThreadDownload {
    public static void main(String[] args) throws Exception {
        String path = "http://file.tstudy.dev/sysfile/templete/import-expert-templete.xls";
        new MulThreadDownload().download(path, 3);
    }

    /**
     * 上传文件
     * 
     * @param path
     * @param i
     * @throws Exception
     */
    private void download(String path, int threadsize) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            int length = conn.getContentLength();// 获取网络文件的长度
            File file = new File(getFilename(path));
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");// 在本地生成一个长度相等的文件
            accessFile.setLength(length);
            accessFile.close();
            // 计算每条线程负责下载的数据量
            int block = length % threadsize == 0 ? length / threadsize : length / threadsize + 1;
            for (int threadid = 0; threadid < threadsize; threadid++) {
                new DownloadThread(threadid, block, url, file).start();
            }
        }
        else {
            System.out.println("上传失败");
        }

    }

    private class DownloadThread extends Thread {
        private int threadid;
        private int block;
        private URL url;
        private File file;

        public DownloadThread(int threadid, int block, URL url, File file) {
            this.threadid = threadid;
            this.block = block;
            this.url = url;
            this.file = file;
        }

        @Override
        public void run() {
            int start = threadid * block; // 计算该线程从网络文件的什么位置开始下载
            int end = (threadid + 1) * block - 1;// 下载到网络文件的的什么位置结束
            try {
                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                accessFile.seek(start);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
                // if(conn.getResponseCode() == 206){
                // System.out.println(conn.getResponseCode());
                InputStream inStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1) {
                    accessFile.write(buffer, 0, len);
                }
                accessFile.close();
                inStream.close();
                // }
                System.out.println("第" + (threadid + 1) + "条线程已经下载完成");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param path
     * @return
     */
    private String getFilename(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
