package com.kevin.utils;

import java.util.Random;

/**
 * Created by kevin on 2016/10/5.
 * 获取各种随机数，或者随机组合的字符串
 */
public class RandomUtils {
    private static final Random random = new Random();
    private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERCHAR = "0123456789";

    /**
     * 获取一定长度的随机字符串，大小写，数字
     * @param length
     * @return
     */
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取一定长度的随机字符串，大小写
     * @param length
     * @return
     */
    public static String generateMixString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }


    /**
     * 获取一定长度的随机字符串，数字
     * @param length
     * @return
     */
    public static String generateNumString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取一定长度小写/大写字母
     * @param length
     * @Upper isUp 是否大写
     * @return
     */
    public static String generateLowerString(int length, boolean isUp) {
        return isUp ? generateMixString(length).toUpperCase() : generateMixString(length).toLowerCase();
    }

    /**
     * 获取一定个数的填充0字符串。
     * @param length
     * @return
     */
    public static String generateZeroString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    /**
     * 根据给定数，返回定长数
     * @param num
     * @param length
     * @return
     */
    public static String fixLengthString(long num, int length) {
        StringBuilder sb = new StringBuilder();
        String numStr = String.valueOf(num);
        if (numStr.length() > length) {
            sb.append(numStr,0,length);
        } else {
            sb.append(generateZeroString(length - numStr.length()) + numStr);
        }
        return sb.toString();
    }

    /**
     * 获取随机数
     */
    public static String generateNum(int min, int max){
        int i = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(i);
    }
}
