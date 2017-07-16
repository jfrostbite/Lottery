package com.kevin.lottery.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2016/10/6.
 */
public class Constant {

    public static final String[] MODEL_PHONE = {"MI-2"
            , "MI-3"
            , "MI-4"
            , "MI-ONE"
            , "MI-ONE-Plus"
            , "SM-G9009D"
            , "SM-I9300"
            , "SM-I9500"
            , "SM-N7100"
            , "SM-I9100"
            , "SM-I9220"
            , "SM-I9508"
            , "SM-I8552"
            , "SM-I9308"
            , "SM-I959"
            , "SM-N9005"
            , "SM-I9502"
            , "SM-N7102"
            , "SM-S7562"
            , "SM-I9100G"
            , "SM-I777"
            , "SM-G385"
            , "SM-G3556D"
            , "SM-N9106W"
            , "SM-N910U"
            , "SM-I500"
            , "SM-T959"
            , "SM-I9158P"
            , "SM-N9108V"
            , "SM-V700"
            , "SM-N9008"
            , "SM-N9005"
            , "SM-N9002"
            , "SM-I929"
            , "SM-N9006"
            , "SM-N7109"
            , "SM-I9305"
            , "SM-S5570"
            , "SM-S7898"
            , "SM-I9158"
            , "SM-G3818"
            , "SM-N9009"
            , "SM-P709"
            , "SM-I9103"
            , "SM-G3508I"
            , "SM-S7278"
            , "SM-I9200"
            , "SM-S5660"
            , "SM-I9001"
            , "SM-N9100"
            , "SM-I9003"
            , "SM-G9006"
            , "SM-I8150"
            , "SM-I9228"
            , "SM-G3812"
            , "SM-S7562C"
            , "SM-S7568I"
            , "SM-N7105"
            , "SM-I939"
            , "SM-9008"
            , "H30-U10"
            , "HUAWEI-C8813"
            , "HUAWEI-C8815"
            , "HUAWEI-C8813Q"
            , "HUAWEI-C8816"
            , "HUAWEI-G520"
            , "HUAWEI-C8813D"
            , "HUAWEI-C8812"
            , "HUAWEI-G330D"
            , "HUAWEI-T510"
            , "HUAWEI-G520-5000"
            , "HUAWEI-G610S"
            , "HUAWEI-B199"
            , "HUAWEI-C8812E"
            , "HUAWEI-G610C"
            , "HUAWEI-G610T-T11"
            , "HUAWEI-C8650"
            , "HUAWEI-U8951"
            , "HUAWEI-C8817D"
            , "HUAWEI-G610T-T10"
            , "HUAWEI-U8860"
            , "HUAWEI-C8816D"
            , "HUAWEI-G525-U00"
            , "HUAWEI-C8825D"
            , "HUAWEI-C8650+"
            , "HUAWEI-Y310-5000"
            , "HUAWEI-C8950D"
            , "HUAWEI-U9200"
            , "HUAWEI-U8950D"
            , "HUAWEI-C8500"
            , "HUAWEI-U8500"
            , "HUAWEI-Y210S"
            , "HUAWEI-C8600"
            , "HUAWEI-U8220"
            , "HUAWEI-U8160"
            , "HTC-Amaze"
            , "HTC-Aria"
            , "HTC-Butterfly"
            , "HTC-Chacha"
            , "HTC-EVO"
            , "HTC-Hero"
            , "HTC-Wildfire"
            , "HTC-Sensation"
            , "HTC-Salsa"
            , "HTC-One"
            , "HTC-Desire"
            , "HTC-One"
            , "HTC-T329t"
            , "HTC-T327t"
            , "HTC-T-Mobile"
            , "HTC-Tattoo"
            , "HTC-Raider"
            , "HTC-Legend"
            , "HTC-Mytouch"
            , "HTC-Magic"
            , "HTC-Incredible"
            , "SCH-NI959"
            , "Lenovo-A820t"
            , "Lenovo-A788t"
            , "Lenovo-A630T"
            , "Lenovo-A820"
            , "Lenovo-A308t"
            , "Lenovo-A800"
            , "Lenovo-K860i"
            , "Lenovo-A320T"
            , "Lenovo-A808t"
            , "Lenovo-A670T"
            , "Lenovo-A850"
            , "Lenovo-A60"
            , "Lenovo-K860"
            , "Lenovo-P700"
            , "Lenovo-S820"
            , "Lenovo-A830"
            , "coolpad-8675"
            , "coolpad-8297"
            , "coolpad-8270L"
            , "coolpad-5950"
            , "coolpad-8675"
            , "coolpad-7295A"
            , "coolpad-8720Q"
            , "coolpad-5951"
            , "coolpad-8705"
            , "coolpad-8670"
            , "coolpad-7296"
            , "coolpad-5891"
            , "coolpad-7295C"
            , "coolpad-8297W"
            , "coolpad-8190Q"
            , "coolpad-9976A"
            , "Google-Nexus"
            , "ZTE-X9180"
            , "ZTE-V880"
            , "ZTE-N909"
            , "ZTE-U819"
            , "ZTE-N880E"
            , "ZTE-V967S"
            , "ZTE-V889D"
            , "ZTE-U956"
            , "ZTE-U930"
            , "ZTE-U950"
            , "ZTE-V970"
            , "ZTE-U970"
            , "ZTE-V988"
            , "ZTE-N988"
            , "ZTE-N760"
            , "ZTE-V987"
            , "ZTE-N880S"};

    /**
     * 360活动主机地址
     */
    public static final String HOST = "http://lottery.mobilem.360.cn/";
//    public static final String HOST = "http://account.shafa.com/api/";

    /**
     * 公共参数
     */
    public static final String QUERY = "/turntable/base";

    /**
     * 通用请求参数
     */
    public static final String ACTIVE = "active";
    public static final String MID = "mid";
    public static final String QID = "qid";
    public static final String VERIFY = "verify";
    public static final String TYPE = "type";
    public static final String DOWN_ = "_";
    public static final String JSCALL = "jscallback";
    public static final String DOWN__ = "__";
    public static final String V = "v";

    public static final String NOTE_NEW = "node/new";
    public static final String POINT_INFO = "points/info";
    public static final String REWARD_LOTTERY = "rewards/lottery";

    public static final String STATUS_OK = "ok";

    public static Map<String, Integer> ips = new HashMap<>();
}

