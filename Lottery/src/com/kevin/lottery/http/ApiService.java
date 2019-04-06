package com.kevin.lottery.http;

import com.kevin.lottery.entity.MiBean;
import com.kevin.lottery.entity.PocoBean;
import com.kevin.lottery.entity.ShafaBean;
import okhttp3.ResponseBody;
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
    Observable<ResponseBody> addChance(@QueryMap Map<String,String> map);

    /**
     * 获取抽奖机会
     */
    @GET(Constant.QUERY+"/getcurtimes")
    Observable<ResponseBody> getChance(@QueryMap Map<String, String> map);

    /**
     * 抽奖
     */
    @GET(Constant.QUERY+"/draw")
    Observable<ResponseBody> startDraw(@QueryMap Map<String,String> map);

    /**
     * 提取代理
     */
    @GET("http://www.89ip.cn/apijk/?&tqsl=30&sxa=&sxb=&tta=&ports=&ktip=&cf=1")
    Observable<ResponseBody> ip();

    /**
     * 提交获奖信息
     */
    @GET(Constant.QUERY+"/useraddress")
    Observable<ResponseBody> sendInfo(@QueryMap Map<String, String> map);

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
    Observable<String> preDraw();

    /**
     * 宝宝故事抽奖
     */
    @FormUrlEncoded
//    @POST("http://m.babystory365.com/activity/thank_draw.htm?actionMethod=doDraw")
    @POST("http://xmt.www.mi.com/index.php?id=9380&do=draw")
    Observable<MiBean> doDraw(@FieldMap Map<String,String> map);

    /**
     * 小米中奖信息
     * @return
     */
    @FormUrlEncoded
    @POST("http://xmt.www.mi.com/index.php?id=9380&do=start")
    Observable<MiBean> preDraw1(@FieldMap Map<String,String> map);

    /**
     * 宝宝故事抽奖提交
     */
    @FormUrlEncoded
//    @POST("http://m.babystory365.com/activity/thank_draw.htm?actionMethod=submit")
    @POST("http://m.babystory365.com/activity/thank_draw.htm?actionMethod=submit")
    Observable<MiBean> submit(@FieldMap Map<String,String> map);

    /**
     * 沙发管家新用户注册
     */
    @FormUrlEncoded
    @POST(Constant.NOTE_NEW)
    Observable<ShafaBean> node_new(@FieldMap Map<String,String> params);

    /**
     * 沙发管家用户信息获取
     */
    @FormUrlEncoded
    @POST(Constant.POINT_INFO)
    Observable<ShafaBean> points_info(@FieldMap Map<String,String> map);

    /**
     * 沙发管家抽奖
     */
    @FormUrlEncoded
    @POST(Constant.REWARD_LOTTERY)
    Observable<ShafaBean> reward_lottery(@FieldMap Map<String,String> map);


}
