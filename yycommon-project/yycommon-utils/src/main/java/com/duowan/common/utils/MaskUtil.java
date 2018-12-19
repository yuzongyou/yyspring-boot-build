package com.duowan.common.utils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/29 9:59
 */
public class MaskUtil {

    private MaskUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static final int MAX_HEAD_LEN = 4;
    public static final int MAX_TAIL_LEN = 4;

    /**
     * 手机号掩码
     *
     * @param mobile  手机号码
     * @param headLen 头部显示几位, 最多是4
     * @param tailLen 尾部显示几位, 最多是4
     * @return 返回掩码格式
     */
    public static String maskMobile(String mobile, int headLen, int tailLen) {
        if (mobile == null || "".equals(mobile.trim())) {
            return mobile;
        }

        return mask(mobile, headLen, tailLen, MAX_HEAD_LEN, MAX_TAIL_LEN);
    }

    /**
     * 邮箱掩码
     *
     * @param email   邮箱地址
     * @param headLen 头部显示几位, 最多是4
     * @param tailLen 尾部显示几位, 最多是4
     * @return 返回掩码格式
     */
    public static String maskEmail(String email, int headLen, int tailLen) {

        if (email == null || "".equals(email.trim())) {
            return email;
        }

        int index = email.indexOf('@');
        String prefix = email.substring(0, index);
        String suffix = email.substring(index);

        return mask(prefix, headLen, tailLen, MAX_HEAD_LEN, MAX_TAIL_LEN) + suffix;
    }

    public static String mask(String value, int headLen, int tailLen, int maxHeadLen, int maxTailLen) {
        if (null == value || "".equals(value.trim())) {
            return value;
        }
        headLen = headLen > maxHeadLen ? maxHeadLen : headLen;
        tailLen = tailLen > maxTailLen ? maxTailLen : tailLen;

        int valLen = value.length();
        if (valLen <= headLen) {
            headLen = valLen / 2;
        }
        if (valLen <= tailLen) {
            tailLen = valLen / 2;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < headLen; ++i) {
            builder.append(value.charAt(i));
        }
        builder.append("***");
        int len = value.length();
        for (int i = 0; i < tailLen; ++i) {
            builder.append(value.charAt(len - tailLen + i));
        }

        return builder.toString();
    }
}
