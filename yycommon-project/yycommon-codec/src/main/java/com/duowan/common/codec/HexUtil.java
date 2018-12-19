package com.duowan.common.codec;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/31 9:53
 */
public class HexUtil {

    private HexUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return EMPTY_BYTE_ARRAY;
        }
        int l = strhex.length();
        if (l % 2 != 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            String stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                sb.append("0");
            }
            sb.append(stmp);
        }
        return sb.toString().toUpperCase();
    }
}
