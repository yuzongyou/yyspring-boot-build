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

    private DownloadUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtil.class);

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
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        InputStream inStream = null;
        try (FileOutputStream fs = new FileOutputStream(saveFile)) {
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();


            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {

                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
            }
        }
    }
}
