package com.kevin.lottery.draws;

import java.util.Map;

/**
 * Created by kevin on 2016/10/16.
 */
public interface Draw<T> {

    /**
     * 抽奖准备
     */
    void preDraw(Map requestMap);

    /**
     * 抽奖
     */
    void draw();

    /**
     * 设置收货地址
     */
    void submit();

    /**
     * 设置监听器
     */
    void setOnDrawListener(OnDrawListener listener);

}
