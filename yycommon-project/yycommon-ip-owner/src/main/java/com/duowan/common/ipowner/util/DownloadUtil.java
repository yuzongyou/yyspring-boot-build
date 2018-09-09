package com.duowan.common.ipowner.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载工具类
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 16:52
 */
public class DownloadUtil {

    private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

    /**
     * 下载文件
     *
     * @param uri      下载地址
     * @param saveFile 保存的路径
     * @return 返回是下载成功
     */
    public static boolean download(String uri, String saveFile) {

        int byteread = 0;

        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {

                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (IOException e) {
            logger.warn(e.getMessage());
            return false;
        } finally {
            if (null != fs) {
                try {
                    fs.close();
                } catch (IOException ignored) {
                }
            }
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
