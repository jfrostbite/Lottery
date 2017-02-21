package com.kevin.lottery.draws;

import com.kevin.lottery.http.ApiService;

import java.util.Map;

/**
 * Created by kevin on 2017/2/1.
 */
public class Draw_Anzhi implements Draw {

    private ApiService mApiService;
    private OnDrawListener mListener;

    public Draw_Anzhi(ApiService apiService){

        mApiService = apiService;
    }

    @Override
    public Draw preDraw(Map requestMap) {
        return null;
    }

    @Override
    public Draw draw() {
        return null;
    }

    @Override
    public void submit(Map map) {

    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {

        mListener = listener;
    }
}
