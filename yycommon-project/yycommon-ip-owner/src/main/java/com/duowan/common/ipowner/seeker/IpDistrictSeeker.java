package com.duowan.common.ipowner.seeker;

import com.duowan.common.ipowner.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Arvin
 */
public class IpDistrictSeeker implements IpSeeker {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpDistrictSeeker.class);

    /**
     * ip文件路径
     */
    private final String ipdatxFilePath;

    public IpDistrictSeeker(String ipdatxFilePath) {
        this.ipdatxFilePath = StringUtils.isBlank(ipdatxFilePath) ? Constants.DEFAULT_IP_DISTRICT_FILE_PATH : ipdatxFilePath;
        load(this.ipdatxFilePath);

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "ipdatxFilePath='" + ipdatxFilePath + '\'' +
                '}';
    }

    private int offset;
    private int[] index = new int[65536];
    private ByteBuffer dataBuffer;
    private ByteBuffer indexBuffer;
    private File ipFile;
    private ReentrantLock lock = new ReentrantLock();

    public void load(String name) {
        ipFile = new File(name);
        load();
    }

    private void load() {
        lock.lock();
        try (FileInputStream fin = new FileInputStream(ipFile)) {
            dataBuffer = ByteBuffer.allocate((int) ipFile.length());
            int readBytesLength;
            byte[] chunk = new byte[4096];
            while (fin.available() > 0) {
                readBytesLength = fin.read(chunk);
                dataBuffer.put(chunk, 0, readBytesLength);
            }
            dataBuffer.position(0);
            int indexLength = dataBuffer.getInt();
            byte[] indexBytes = new byte[indexLength];
            dataBuffer.get(indexBytes, 0, indexLength - 4);
            indexBuffer = ByteBuffer.wrap(indexBytes);
            indexBuffer.order(ByteOrder.LITTLE_ENDIAN);
            offset = indexLength;

            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    index[i * 256 + j] = indexBuffer.getInt();
                }
            }
            indexBuffer.order(ByteOrder.BIG_ENDIAN);
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String[] find(String ip) {
        String[] ips = ip.split("\\.");
        int prefixValue = (Integer.valueOf(ips[0]) * 256 + Integer.valueOf(ips[1]));
        long ip2LongValue = ip2long(ip);
        int start = index[prefixValue];
        int maxCompLen = offset - 262148;
        long indexOffset = -1;
        int indexLength = -1;
        for (start = start * 13 + 262144; start < maxCompLen; start += 13) {
            boolean needBreak = false;
            if (int2long(indexBuffer.getInt(start)) <= ip2LongValue) {
                if (int2long(indexBuffer.getInt(start + 4)) >= ip2LongValue) {
                    indexOffset = bytesToLong(indexBuffer.get(start + 11), indexBuffer.get(start + 10), indexBuffer.get(start + 9), indexBuffer.get(start + 8));
                    indexLength = 0xFF & indexBuffer.get(start + 12);
                    needBreak = true;
                }
            } else {
                needBreak = true;
            }
            if (needBreak) {
                break;
            }
        }

        if (indexOffset == -1 && indexLength == -1) {
            return new String[0];
        }

        byte[] areaBytes;
        lock.lock();
        try {
            dataBuffer.position(offset + (int) indexOffset - 262144);
            areaBytes = new byte[indexLength];
            dataBuffer.get(areaBytes, 0, indexLength);
        } finally {
            lock.unlock();
        }

        return new String(areaBytes, Charset.forName("UTF-8")).split("\t", -1);
    }

    private long bytesToLong(byte a, byte b, byte c, byte d) {
        return int2long((((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff)));
    }

    private int str2Ip(String ip) {
        String[] ss = ip.split("\\.");
        int a = Integer.parseInt(ss[0]);
        int b = Integer.parseInt(ss[1]);
        int c = Integer.parseInt(ss[2]);
        int d = Integer.parseInt(ss[3]);
        return (a << 24) | (b << 16) | (c << 8) | d;
    }

    private long ip2long(String ip) {
        return int2long(str2Ip(ip));
    }

    private long int2long(int i) {
        long l = i & 0x7fffffffL;
        if (i < 0) {
            l |= 0x080000000L;
        }
        return l;
    }
}
