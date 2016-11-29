package com.kevin.lottery.draws;

import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiSubscriber;
import rx.Observable;

import java.util.Map;

/**
 * Created by kevin on 2016/11/29.
 */
public class Draw_Baby implements Draw {

    private ApiService mApi;
    private int mIndex;

    public Draw_Baby(int index, ApiService apiService) {
        mIndex = index;

        mApi = apiService;
    }

    @Override
    public Draw preDraw(Map requestMap) {
        Observable preDraw = mApi.preDraw();
        preDraw.subscribe(new ApiSubscriber() {
            @Override
            public void onSuccess(Object o) {

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
    public Draw draw() {
        return this;
    }


    public Draw draw(Map map) {
        mApi.doDraw(map);
        return this;
    }

    @Override
    public void submit(Map map) {

    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {

    }

    public int getIndex() {
        return mIndex;
    }

    public Map<String, String> generateMap() {
        return null;
    }
}
