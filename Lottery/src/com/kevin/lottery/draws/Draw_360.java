package com.kevin.lottery.draws;

import com.google.gson.Gson;
import com.kevin.lottery.entity.BaseBean;
import com.kevin.lottery.entity.LotteryBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiSubscriber;
import com.kevin.lottery.http.Constant;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.time.Instant;
import java.util.Map;

/**
 * Created by kevin on 2016/10/16.
 */
public class Draw_360 implements Draw<Void>{

    private ApiService apiService;
    private OnDrawListener mListener;

    public Draw_360(ApiService apiService){
        super();

        this.apiService = apiService;
    }

    @Override
    public void preDraw(Map requestMap) {
        addChance(requestMap);
    }

    @Override
    public void draw() {

    }

    @Override
    public void submit() {

    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {

        mListener = listener;
    }

    /**
     * 增加抽奖机会
     *
     * @param requestMap
     */
    private void addChance(Map<String, String> requestMap) {
        Observable<BaseBean<String>> observable = apiService.addChance(requestMap);
        observable.observeOn(Schedulers.io())
                .map((bean) -> {
                    String msg = "增加抽奖机会失败！>>> ";
                    if (Constant.STATUS_OK.equals(bean.status)) {
                        msg = "增加抽奖机会成功！机会+" + bean.data;
                    } else {
                        msg += bean.data;
                    }
                    return msg;
                })
                .subscribe(new ApiSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (mListener!=null) {
                            mListener.onDraw(s);
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (mListener!=null) {
                            mListener.onDraw("错误：" + code + "；" + msg);
                        }
                    }

                    @Override
                    public void onFinish() {
                        getChance(requestMap);

                    }
                });
    }


    /**
     * 获取机会
     *
     * @param requestMap
     */
    private void getChance(Map<String, String> requestMap) {
        requestMap.remove(Constant.TYPE);
        requestMap.remove(Constant.VERIFY);
        Observable<BaseBean<String>> observable = apiService.getChance(requestMap);
        observable.subscribeOn(Schedulers.io())
                .map(bean -> {
                    return bean.data;
                })
                .subscribe(new ApiSubscriber<String>() {

                    private int chance;

                    @Override
                    public void onSuccess(String s) {
                        if (mListener!=null) {
                            mListener.onDraw(s + "\n");
                        }
                        chance = Integer.parseInt(s);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (mListener!=null) {
                            mListener.onDraw("错误：" + code + "；" + msg);
                        }
                    }

                    @Override
                    public void onFinish() {
                        for (int i = 0; i < chance; i++) {
                            startDraw(requestMap);
                        }
                    }
                });
    }

    /**
     * 抽奖
     *
     * @param requestMap
     */
    private void startDraw(Map<String, String> requestMap) {
        String remove = requestMap.remove(Constant.QID);
        if (remove != null) {
            requestMap.put(Constant.MID, remove);
        }
        requestMap.put(Constant.DOWN__, String.valueOf(Instant.now().getEpochSecond()));
        apiService.startDraw(requestMap)
                .map(bean -> {
                    LotteryBean lotteryBean = null;
                    if (Constant.STATUS_OK.equals(bean.status)) {
                        lotteryBean = new Gson().fromJson(bean.data, LotteryBean.class);
                        lotteryBean.hasPrize = true;
                    } else {
                        lotteryBean = new LotteryBean();
                        lotteryBean.drawmark = bean.data;
                        lotteryBean.hasPrize = false;
                    }
                    return lotteryBean;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<LotteryBean>() {
                    @Override
                    public void onSuccess(LotteryBean lotteryBean) {
                        if (lotteryBean.hasPrize) {
                            if (mListener!=null) {
                                mListener.onDraw(remove+";恭喜获得：" + lotteryBean.drawmark);
                                mListener.alertDialog(lotteryBean.drawmark);
                            }
                        } else {
                            if (mListener!=null) {
                                mListener.onDraw("很遗憾，未中奖！>>> " +lotteryBean.drawmark);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (mListener!=null) {
                            mListener.onDraw(msg);
                        }

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }


    /**
     * 提交信息逻辑
     */
    private void submitInfo() {
    }
}
