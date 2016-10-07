package com.kevin.lottery.http;

import rx.Subscriber;

import javax.xml.ws.http.HTTPException;

/**
 * Created by kevin on 2016/10/6.
 * retrofit网络请求框架中，请求处理回掉通用方法
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted(){
        onFinish();
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof HTTPException) {
            HTTPException exception = (HTTPException) throwable;
            int statusCode = exception.getStatusCode();
            String message = exception.getMessage();
            if (502 == statusCode || 404 == statusCode) {
                message = "服务器异常，请稍后再试";
            } else if(504 == statusCode){
                message = "网络不给力";
            }
            onFailure(statusCode,message+"\n");
        } else {
            onFailure(0, throwable.getMessage()+"\n");
        }
        onFinish();
    }

    @Override
    public void onNext(T t){
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
    public abstract void onFailure(int code, String msg);
    public abstract void onFinish();
}
