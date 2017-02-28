package com.kevin.lottery.draws;

import com.kevin.lottery.entity.MiBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiSubscriber;
import com.kevin.utils.RandomUtils;
import com.kevin.utils.TextUtils;
import rx.Observable;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kevin on 2017/2/20.
 */
public class Draw_Mi implements Draw {

    private int mIndex;
    private ApiService mApi;
    private OnDrawListener mListener;
    private Map mRequestMap;
    private String result;

    public Draw_Mi(int index, ApiService api) {
        mIndex = index;

        mApi = api;
    }

    @Override
    public Draw preDraw(Map requestMap) {
        mRequestMap = requestMap;
        mApi.preDraw1(new TreeMap<>()).subscribe(new ApiSubscriber<MiBean>() {
            @Override
            public void onSuccess(MiBean miBean) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }

            @Override
            public void onFinish() {
                try {
                    Thread.sleep(1000);
                    draw();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    @Override
    public Draw draw() {
        Observable<MiBean> observable = mApi.doDraw(mRequestMap);
        observable.map(bean -> {
            result = TextUtils.isEmpty(bean.body.data.name) ? result : bean.body.data.name;
            switch (bean.body.data.type) {
                case "0":
//                    result = "再接再厉";
                    break;
                case "2":
                    mListener.saveLog(bean.code + "---" + bean.body.data.name + "\n");
                    break;
                default:
                    break;
            }
            return result;
        }).subscribe(new ApiSubscriber<String>() {
            @Override
            public void onSuccess(String r) {

                mListener.onDraw(mIndex, r);

            }

            @Override
            public void onFailure(int code, String msg) {
                mListener.onDraw(mIndex, msg);
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

        mListener = listener;
    }

    public Map<String, String> generateMap() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("color", RandomUtils.generateNum(1, 5));
        return map;
    }

    public int getIndex() {
        return mIndex;
    }
}
