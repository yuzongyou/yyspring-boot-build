package com.duowan.common.ipowner.seeker;

import com.duowan.common.ipowner.util.Constants;
import org.apache.commons.lang3.StringUtils;

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
public class IpProvinceAndCitySeeker implements IpSeeker {

    /**
     * ip文件路径
     */
    private final String ipdatxFilePath;

    public IpProvinceAndCitySeeker(String ipdatxFilePath) {
        this.ipdatxFilePath = StringUtils.isBlank(ipdatxFilePath) ? Constants.DEFAULT_IP_PROVINCE_CITY_FILE_PATH : ipdatxFilePath;
        load(this.ipdatxFilePath, true);
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

    private void load(String filename) {
        ipFile = new File(filename);
        load();
    }

    private void load(String filename, boolean strict) {
        ipFile = new File(filename);
        if (strict) {
            int contentLength = Long.valueOf(ipFile.length()).intValue();
            if (contentLength < 512 * 1024) {
                throw new RuntimeException("ip data file error.");
            }
        }
        load();
    }

    @Override
    public String[] find(String ip) {
        String[] ips = ip.split("\\.");
        int prefix_value = (Integer.valueOf(ips[0]) * 256 + Integer.valueOf(ips[1]));
        long ip2long_value = ip2long(ip);
        int start = index[prefix_value];
        int max_comp_len = offset - 262144 - 4;
        long tmpInt;
        long index_offset = -1;
        int index_length = -1;
        byte b = 0;
        for (start = start * 9 + 262144; start < max_comp_len; start += 9) {
            tmpInt = int2long(indexBuffer.getInt(start));
            if (tmpInt >= ip2long_value) {
                index_offset = bytesToLong(b, indexBuffer.get(start + 6), indexBuffer.get(start + 5), indexBuffer.get(start + 4));
                index_length = (0xFF & indexBuffer.get(start + 7) << 8) + (0xFF & indexBuffer.get(start + 8));
                break;
            }
        }

        byte[] areaBytes;

        lock.lock();
        try {
            dataBuffer.position(offset + (int) index_offset - 262144);
            areaBytes = new byte[index_length];
            dataBuffer.get(areaBytes, 0, index_length);
        } finally {
            lock.unlock();
        }

        return new String(areaBytes, Charset.forName("UTF-8")).split("\t", -1);
    }

    private void load() {
        lock.lock();
        try {
            dataBuffer = ByteBuffer.wrap(getBytesByFile(ipFile));
            dataBuffer.position(0);
            offset = dataBuffer.getInt(); // indexLength
            byte[] indexBytes = new byte[offset];
            dataBuffer.get(indexBytes, 0, offset - 4);
            indexBuffer = ByteBuffer.wrap(indexBytes);
            indexBuffer.order(ByteOrder.LITTLE_ENDIAN);

            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    index[i * 256 + j] = indexBuffer.getInt();
                }
            }
            indexBuffer.order(ByteOrder.BIG_ENDIAN);
        } finally {
            lock.unlock();
        }
    }

    private byte[] getBytesByFile(File file) {
        FileInputStream fin = null;
        byte[] bs = new byte[new Long(file.length()).intValue()];
        try {
            fin = new FileInputStream(file);
            int readBytesLength = 0;
            int i;
            while ((i = fin.available()) > 0) {
                fin.read(bs, readBytesLength, i);
                readBytesLength += i;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bs;
    }

    private long bytesToLong(byte a, byte b, byte c, byte d) {
        return int2long((((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff)));
    }

    private int str2Ip(String ip) {
        String[] ss = ip.split("\\.");
        int a, b, c, d;
        a = Integer.parseInt(ss[0]);
        b = Integer.parseInt(ss[1]);
        c = Integer.parseInt(ss[2]);
        d = Integer.parseInt(ss[3]);
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
