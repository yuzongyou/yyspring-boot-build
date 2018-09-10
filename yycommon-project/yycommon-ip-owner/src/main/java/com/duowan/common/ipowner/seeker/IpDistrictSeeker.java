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
public class IpDistrictSeeker implements IpSeeker {

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
        FileInputStream fin = null;
        lock.lock();
        try {
            dataBuffer = ByteBuffer.allocate(Long.valueOf(ipFile.length()).intValue());
            fin = new FileInputStream(ipFile);
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
            lock.unlock();
        }
    }

    @Override
    public String[] find(String ip) {
        String[] ips = ip.split("\\.");
        int prefix_value = (Integer.valueOf(ips[0]) * 256 + Integer.valueOf(ips[1]));
        long ip2long_value = ip2long(ip);
        int start = index[prefix_value];
        int max_comp_len = offset - 262148;
        long index_offset = -1;
        int index_length = -1;
        for (start = start * 13 + 262144; start < max_comp_len; start += 13) {
            if (int2long(indexBuffer.getInt(start)) <= ip2long_value) {
                if (int2long(indexBuffer.getInt(start + 4)) >= ip2long_value) {
                    index_offset = bytesToLong(indexBuffer.get(start + 11), indexBuffer.get(start + 10), indexBuffer.get(start + 9), indexBuffer.get(start + 8));
                    index_length = 0xFF & indexBuffer.get(start + 12);
                    break;
                }
            } else {
                break;
            }
        }

        if (index_offset == -1 && index_length == -1) {
            return null;
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
