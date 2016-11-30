package com.kevin.lottery.draws;

import com.kevin.lottery.entity.PocoBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiSubscriber;
import rx.Observable;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kevin on 2016/11/29.
 */
public class Draw_Baby implements Draw {

    private ApiService mApi;
    private int mIndex;
    private OnDrawListener mListener;
    private Map mRequestMap;

    public Draw_Baby(int index, ApiService apiService) {
        mIndex = index;

        mApi = apiService;
    }

    @Override
    public Draw preDraw(Map requestMap) {
        mRequestMap = requestMap;
        Observable<String> preDraw = mApi.preDraw();
        preDraw.subscribe(new ApiSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                mListener.onDraw(mIndex,"正在抽奖...");
            }

            @Override
            public void onFailure(int code, String msg) {
                mListener.onDraw(mIndex,"正在抽奖...");
            }

            @Override
            public void onFinish() {
                mListener.onDraw(mIndex,"正在抽奖...");
            }
        });
        return this;
    }

    @Override
    public Draw draw() {
        return this;
    }


    public Draw draw(Map map) {
        Observable<PocoBean> observable = mApi.doDraw(map);
        observable.map(bean -> {
            bean.win = true;
            switch (bean.result) {
                case "1":
                    bean.prize = "机器人";
                    bean.big = true;
                    break;
                case "2":
                    bean.prize = "电饭煲";
                    bean.big = true;
                    break;
                case "3":
                    bean.prize = "面膜";
                    bean.big = false;
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
        }).subscribe(new ApiSubscriber<PocoBean>() {
            @Override
            public void onSuccess(PocoBean pocoBean) {
                mListener.onDraw(mIndex, pocoBean.prize);
                if (pocoBean.win) {
                    mListener.saveLog(pocoBean.prize, pocoBean.code + "---" + pocoBean.prize +"\n");
                    if (pocoBean.big) {
                        mRequestMap.put("code",pocoBean.code);
                        submit(mRequestMap);
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

            }
        });
            return this;
        }

        @Override
        public void submit (Map map){
            Observable<PocoBean> observable = mApi.submit(map);
            observable.subscribe(new ApiSubscriber<PocoBean>() {
                @Override
                public void onSuccess(PocoBean pocoBean) {
                    mListener.saveLog(pocoBean.result
                            ,pocoBean.message);
                    mListener.onDraw(mIndex,pocoBean.message);
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
        public void setOnDrawListener (OnDrawListener listener){

            mListener = listener;
        }

    public int getIndex() {
        return mIndex;
    }

    public Map<String, String> generateMap() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("channel", "xmthank");
        map.put("versionCode", "2.0.1");
        return map;
    }
}
