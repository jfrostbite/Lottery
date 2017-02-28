package com.kevin.lottery.draws;

import com.kevin.lottery.entity.BaseBean;
import com.kevin.lottery.entity.LotteryBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiSubscriber;
import rx.Observable;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xianda on 2017/2/28.
 */
public class Draw_51talk implements Draw<String> {

    private int mIndex;
    private ApiService mApi;
    private Map<String,String> mMap;
    private OnDrawListener mListener;

    public Draw_51talk(int index, ApiService api){
        mIndex = index;

        mApi = api;
    }

    @Override
    public Draw preDraw(Map<String,String> requestMap) {
        mMap = requestMap;
        String cookie = requestMap.get("cookie1");
        String openid = requestMap.get("openid");
        Observable<BaseBean<LotteryBean>> observable = mApi.give(cookie, openid);
        observable.subscribe(new ApiSubscriber<BaseBean<LotteryBean>>() {
            @Override
            public void onSuccess(BaseBean<LotteryBean> lotteryBean) {
                if ("1".equals(lotteryBean.status)) {
                    mListener.onDraw(mIndex,"增加次数："+lotteryBean.info);
                    draw();
                } else {
                    mListener.onDraw(mIndex,"增加次数："+lotteryBean.info);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mListener.onDraw(mIndex,"增加次数失败");
            }

            @Override
            public void onFinish() {

            }
        });
        return this;
    }

    @Override
    public Draw draw() {
        String cookie = mMap.get("cookie2");
        Observable<BaseBean<LotteryBean>> observable = mApi.getAward(cookie);
        observable.subscribe(new ApiSubscriber<BaseBean<LotteryBean>>() {
            @Override
            public void onSuccess(BaseBean<LotteryBean> lotteryBeanBaseBean) {
                mListener.onDraw(mIndex,"抽奖："+lotteryBeanBaseBean.info+"["+lotteryBeanBaseBean.data.angle+"]");
                switch (lotteryBeanBaseBean.data.angle) {
                    case "2":
                        mListener.saveLog(lotteryBeanBaseBean);
                        break;
                    case "4":
                    case "6":
                    case "8":
                    case "9":
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
        return this;
    }

    @Override
    public void submit(Map map) {

    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {

        this.mListener = listener;
    }

    public Map<String,String> generateMap(){
        TreeMap<String, String> map = new TreeMap<>();
        map.put("openid", "o2LKAwPrPQCSRUpOVnuhPIOSj-Ug");
        map.put("cookie1", "nat_activity_openid=bNzTJFM0SY0WFx3rRMWjJAvxQMnQFO0O0OWO0O0OblZQS3JPbF8yVGMtcEhWdwO0O0OO0O0O");//加次数
        map.put("cookie2", "nat_activity_openid=bNzTJFM0SY0WFx3rUMHjJAQxUMUQNO0O0OTO0O0OUlVwT1ZudWhQSU9Tai1VZwO0O0OO0O0O");//draw
        return map;
    }
}
