package com.kevin.lottery.entity;

/**
 * Created by kevin on 2016/10/5.
 */
public class RequestBean {

    public String mid,active,qid,type,verify;
    public String down_;
    public String down__;
    public RequestBean(){
        down_ = String.valueOf(System.currentTimeMillis());
        down__ = String.valueOf(System.currentTimeMillis() + 1234);
    }
}
