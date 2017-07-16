package com.kevin.lottery.draws;

import com.kevin.lottery.http.ApiService;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kevin on 2017/6/3.
 */
public class Draw_Shafa implements Draw {

    private ApiService mApiService;

    public Draw_Shafa(ApiService apiService){

        mApiService = apiService;
    }

    @Override
    public Draw preDraw(Map requestMap) {

//        Observable<ShafaBean> shafaBeanObservable = mApiService.node_new();
        return this;
    }

    @Override
    public Draw draw() {
        return this;
    }

    @Override
    public void submit(Map map) {

    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {

    }

    public Map<String,String> generateMap(){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("device_name", "");
        params.put("mac", "");
        params.put("ep", "556");
        params.put("time", System.currentTimeMillis()+"");
        return null;
    }
}
