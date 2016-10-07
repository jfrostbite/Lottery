package com.kevin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by kevin on 2016/10/6.
 * 文本工具类
 */
public class TextUtils {
    /**
     * 文本非空判断
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string);
    }

    /**
     * md5加密
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (string == null || "".equals(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        return "【"+format+"】";
    }
}
