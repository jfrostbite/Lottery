package com.kevin.lottery.http;

import com.kevin.lottery.entity.BaseBean;
import com.kevin.lottery.entity.LotteryBean;
import com.kevin.lottery.entity.LuckerBean;
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
    Observable<LuckerBean> addChance(@QueryMap Map<String,String> map);

    /**
     * 获取抽奖机会
     */
    @GET(Constant.QUERY+"/getcurtimes")
    Observable<LuckerBean> getChance(@QueryMap Map<String, String> map);

    /**
     * 抽奖
     */
    @GET(Constant.QUERY+"/draw")
    Observable<LotteryBean> startDraw(@QueryMap Map<String,String> map);

    /**
     * 提交获奖信息
     */
    @GET(Constant.QUERY+"/useraddress")
    Observable<BaseBean<String>> sendInfo(@QueryMap Map<String, String> map);
}
