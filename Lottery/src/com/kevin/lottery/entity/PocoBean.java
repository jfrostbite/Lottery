package com.kevin.lottery.entity;

/**
 * Created by kevin on 2016/11/27.
 */
public class PocoBean {
    //{"change":1,"prize":1,"angle":18,"winkey":"1a68a4ca5c8069996731fe2a74e21894","record":1705}
    public String prize,winkey,record,ok,err,result,code,time,message;
    public boolean win,big;

    public String getResult() {
        return result;
    }

    public void setresult(String result) {
        this.result = result;
    }
}
