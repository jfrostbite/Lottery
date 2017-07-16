package com.kevin.lottery.draws;

import com.kevin.lottery.http.ApiStore;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * Created by kevin on 2017/3/12.
 *
 */
public class Draw_Duiba implements Draw {

    private ApiStore mApi;
    private int mIndex;
    private OnDrawListener mListener;
    private Map mRequestMap;

    public Draw_Duiba(ApiStore api, int index){

        mApi = api;
        mIndex = index;
    }

    @Override
    public Draw preDraw(Map requestMap) {
        mRequestMap = requestMap;
        return this;
    }

    private Draw doJoin(){

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

        mListener = listener;
    }

    private String getToken(String tokenStr){
        try {
            String jsFun = tokenStr.substring(tokenStr.indexOf("/*"),tokenStr.indexOf("\\u0065\\u0076\\u0061\\u006c"));
            String jsEval = tokenStr.substring(tokenStr.indexOf("\\u0065\\u0076\\u0061\\u006c"),tokenStr.indexOf(";/")+1);
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine js = manager.getEngineByName("js");
            js.eval(jsFun);
            return (String) js.eval(jsEval);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return "";
    }
}
