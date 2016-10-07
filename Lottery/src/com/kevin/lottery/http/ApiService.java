package com.kevin.lottery.http;

import com.kevin.lottery.entity.BaseBean;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
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
}
