package com.kevin.lottery.draws;

import com.kevin.lottery.entity.PocoBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiSubscriber;
import com.kevin.lottery.utils.MD5Utils;
import com.kevin.utils.RandomUtils;
import rx.Observable;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by kevin on 2016/11/27.
 */
public class Draw_Poco implements Draw {


    private ApiService mApi;
    private int mIndex;
    private Map<String,String> mRequestMap,mInfoMap;
    private OnDrawListener mListener;
    private String mTokenKey;

    public Draw_Poco(int index, ApiService api) {
        mIndex = index;

        mApi = api;
    }


    @Override
    public Draw preDraw(Map requestMap) {
        mRequestMap = new TreeMap<String,String>();
        mInfoMap = new TreeMap<String,String>();
        Set<Map.Entry<String,String>> set = requestMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if ("key".equals(entry.getKey()) || "ch".equals(entry.getKey())) {
                mRequestMap.put(entry.getKey(), entry.getValue());
            }

            if (!"key".equals(entry.getKey())) {
                mInfoMap.put(entry.getKey(), entry.getValue());
            }
        }
        mTokenKey = mRequestMap.get("key");
        return this;
    }

    @Override
    public Draw draw() {
        Observable<PocoBean> observable = mApi.ajaxPrize("TOKEN_KEY=" + mTokenKey, mRequestMap);
        observable
                .map(bean -> {
                    bean.win = true;
                    switch (bean.prize) {
                        case "1":
                            bean.prize = "索尼靓咔";
                            bean.big = true;
                            break;
                        case "2":
                            bean.prize = "灰姑娘 青春萝莉 随心包";
                            bean.big = true;
                            break;
                        case "3":
                            bean.prize = "丝芙兰蜜吻润唇球";
                            bean.big = true;
                            break;
                        case "4":
                            bean.prize = "再来一次";
                            bean.win = false;
                            break;
                        default:
                            bean.win = false;
                            bean.prize = "谢谢参与";
                            break;
                    }
                    return bean;
                })
                .subscribe(new ApiSubscriber<PocoBean>() {
                    @Override
                    public void onSuccess(PocoBean pocoBean) {
                        mListener.onDraw(mIndex, pocoBean.prize);
                        if (pocoBean.win) {
                            mListener.saveLog(pocoBean.prize, mTokenKey + "----winkey----" +pocoBean.winkey + "---" + pocoBean.record +"\n");
                            if (pocoBean.big) {
                                mInfoMap.put("winkey",pocoBean.winkey);
                                mInfoMap.put("record",pocoBean.record);
                                submit(mInfoMap);
                                mListener.alertDialog(pocoBean.prize);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mListener.onDraw(mIndex, msg);
                    }

                    @Override
                    public void onFinish() {
                        System.out.println();
                    }
                });
        return this;
    }

    @Override
    public void submit(Map map) {
        map.remove("key");
        Observable<PocoBean> observable = mApi.ajaxSave("TOKEN_KEY=" + mTokenKey, map);
        observable.subscribe(new ApiSubscriber<PocoBean>() {
            @Override
            public void onSuccess(PocoBean pocoBean) {
                mListener.saveLog(pocoBean.ok
                ,pocoBean.err);
                mListener.onDraw(mIndex,pocoBean.err);
            }

            @Override
            public void onFailure(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {

        mListener = listener;
    }


    public Map<String, String> generateMap() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("key", MD5Utils.md5(RandomUtils.generateString(9)));
        map.put("ch", "interphoto_201612");
        return map;
    }

    public int getIndex() {
        return mIndex;
    }
}
