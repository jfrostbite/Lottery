package com.kevin.lottery.http;

import com.kevin.lottery.entity.BaseBean;
import com.kevin.lottery.entity.PocoBean;
import retrofit2.http.*;
import rx.Observable;

import java.util.Map;

/**
 * Created by kevin on 2016/10/5.
 */
public interface ApiService {

    /**
     * 增加抽奖机会
     */
    @GET(Constant.QUERY+"/outdealtimes")
    Observable<BaseBean<String>> addChance(@QueryMap Map<String,String> map);

    /**
     * 获取抽奖机会
     */
    @GET(Constant.QUERY+"/getcurtimes")
    Observable<BaseBean<String>> getChance(@QueryMap Map<String, String> map);

    /**
     * 抽奖
     */
    @GET(Constant.QUERY+"/draw")
    Observable<BaseBean<String>> startDraw(@QueryMap Map<String,String> map);

    /**
     * 提交获奖信息
     */
    @GET(Constant.QUERY+"/useraddress")
    Observable<BaseBean<String>> sendInfo(@QueryMap Map<String, String> map);

    /**
     * poco抽奖
     */
    @FormUrlEncoded
    @POST("http://www1.poco.cn/topic/qing_special/app_rotate/ajaxPrize.php")
    Observable<PocoBean> ajaxPrize(@Header("Cookie") String cookie, @FieldMap Map<String,String> fieldMap);

    /**
     * 提交
     */
    @FormUrlEncoded
    @POST("http://www1.poco.cn/topic/qing_special/app_rotate/ajaxSave.php")
    Observable<PocoBean> ajaxSave(@Header("Cookie") String cookie, @FieldMap Map<String, String> fieldMap);

    /**
     * 宝宝故事抽奖主页
     */
    @GET("http://m.babystory365.com/activity/thank_draw.htm?code=&versionCode=2.0.1&channel=xmthank")
    Observable preDraw();

    /**
     * 宝宝故事抽奖
     */
    @FormUrlEncoded
    @POST("http://m.babystory365.com/activity/thank_draw.htm?actionMethod=doDraw")
    Observable doDraw(@FieldMap Map<String,String> map);
}
