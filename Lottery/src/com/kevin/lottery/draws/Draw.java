package com.kevin.lottery.draws;

import java.util.Map;

/**
 * Created by kevin on 2016/10/16.
 */
public interface Draw<T> {

    /**
     * 抽奖准备
     */
    Draw preDraw(Map requestMap);

    /**
     * 抽奖
     */
    Draw draw();

    /**
     * 设置收货地址
     */
    void submit(Map map);

    /**
     * 设置监听器
     */
    void setOnDrawListener(OnDrawListener listener);

}
