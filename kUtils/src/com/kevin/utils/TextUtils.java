package com.kevin.utils;

import java.io.*;
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
        return string == null || "".equals(string) || "null".equals(string);
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

    /**
     * 获取文件当中内容
     */
    public static String file2String(File file) {
        if(!file.exists()){
            return null;
        }
        BufferedReader br = null;
        String datas = "";
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                datas += tmp;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            datas = "文件解析错误";
        } catch (IOException e) {
            e.printStackTrace();
//            datas = "文件解析错误";
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }

    /**
     * 字符串序列化
     */
    public static boolean string2File(String datas, String fileName, boolean append) {

        String path = System.getProperty("user.dir");
        File file = new File(path,fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
//            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

            BufferedWriter bw = new BufferedWriter(new FileWriter(file,append));
            bw.write(datas);
            bw.newLine();
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
