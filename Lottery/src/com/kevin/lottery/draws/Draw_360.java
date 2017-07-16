package com.kevin.lottery.draws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kevin.lottery.entity.BaseBean;
import com.kevin.lottery.entity.LotteryBean;
import com.kevin.lottery.entity.RequestBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiStore;
import com.kevin.lottery.http.ApiSubscriber;
import com.kevin.lottery.http.Constant;
import com.kevin.lottery.utils.MD5Utils;
import com.kevin.utils.RandomUtils;
import com.kevin.utils.TextUtils;
import okhttp3.ResponseBody;
import rx.Observable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kevin on 2016/10/16.
 */
public class Draw_360 implements Draw{

    private ApiService apiService;
    private OnDrawListener mListener;
    private RequestBean requestBean;
    private Map<String,String> map;
    private String type;
    private ApiStore mApiStore;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private volatile int index;

    public Draw_360(ApiService apiService){
        super();

        this.apiService = apiService;
        requestBean = new RequestBean();
    }

    @Override
    public Draw preDraw(Map requestMap) {
        addChance(requestMap);
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

    /**
     * 增加抽奖机会
     *
     * @param requestMap
     */
    private void addChance(Map<String, String> requestMap) {
        Observable<ResponseBody> observable = apiService.addChance(requestMap);
        observable.map((str) -> {
            BaseBean<String> bean = null;
            try {
                bean = new Gson().fromJson(str.string(), new TypeToken<BaseBean<String>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                            mListener.onDraw(index,s);
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (mListener!=null) {
                            mListener.onDraw(index,"错误：" + code + "；" + msg);
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
        Observable<ResponseBody> observable = apiService.getChance(requestMap);
        observable.map(str -> {
            BaseBean<String> bean = null;
            try {
                bean = new Gson().fromJson(str.string(), new TypeToken<BaseBean<String>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bean.data;
                })
                .subscribe(new ApiSubscriber<String>() {

                    private int chance;

                    @Override
                    public void onSuccess(String s) {
                        if (mListener!=null) {
                            mListener.onDraw(index,s + "\n");
                        }
                        chance = Integer.parseInt(s);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (mListener!=null) {
                            setProxy(true);
                            mListener.onDraw(index,"错误：" + code + "；" + msg);
                        }
                    }

                    @Override
                    public void onFinish() {
                        for (int i = 0; i < chance; i++) {
                            startDraw(requestMap);
                            break;
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
        String mid = requestMap.get(Constant.MID);
        requestMap.put(Constant.DOWN__, String.valueOf(Instant.now().getEpochSecond()));
        requestMap.put(Constant.JSCALL, "jsonp4");
        apiService.startDraw(requestMap)
                .map(res -> {
                    String str = null;
                    try {
                        str = res.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    str = str.substring(str.indexOf("(")+1,str.indexOf(")"));
                    BaseBean<String> bean = new Gson().fromJson(str, new TypeToken<BaseBean<String>>() {
                    }.getType());
                    LotteryBean lotteryBean = null;
                    if (Constant.STATUS_OK.equals(bean.status) || !"error".equals(bean.status)) {
                        lotteryBean = new Gson().fromJson(bean.data, LotteryBean.class);
                        lotteryBean.hasPrize = true;
                    } else {
                        lotteryBean = new LotteryBean();
                        lotteryBean.drawmark = bean.data+"--"+bean.msg;
                        lotteryBean.hasPrize = false;
                        lotteryBean.address = !TextUtils.isEmpty(bean.msg);
                    }
                    return lotteryBean;
                })
                .subscribe(new ApiSubscriber<LotteryBean>() {
                    @Override
                    public void onSuccess(LotteryBean lotteryBean) {
                        if (lotteryBean.hasPrize) {
                            if (mListener!=null) {
                                mListener.saveLog(lotteryBean.drawmark, mid +"\n");
                                mListener.onDraw(index,";恭喜获得：" + lotteryBean.drawmark);
//                                mListener.alertDialog(lotteryBean.drawmark,mid);
                            }
                        } else {
                            if (mListener!=null) {
                                setProxy(lotteryBean.address);
                                mListener.onDraw(index,"很遗憾，未中奖！>>> " +lotteryBean.drawmark);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (mListener!=null) {
                            setProxy(true);
                            mListener.onDraw(index,msg);
                        }

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void setProxy(boolean address) {
        if (address) {
            if (mListener != null) {
                mListener.onDraw(index,"黑地址");
            }
            Iterator<Map.Entry<String, Integer>> iterator = Constant.ips.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<String, Integer> next = iterator.next();
                InetSocketAddress isa = new InetSocketAddress(next.getKey(), next.getValue());
                boolean unresolved = isa.isUnresolved();
                if (unresolved) {
                    if (mListener != null) {
                        mListener.onDraw(index,"代理IP无效");
                    }
                    return;
                }
                mApiStore.setProxy(new Proxy(Proxy.Type.HTTP,isa));
                apiService = mApiStore.getService(ApiService.class);
                iterator.remove();
            }
        }
    }


    /**
     * 提交信息逻辑
     */
    private void submitInfo() {
    }

    /**
     * 获取通用参数,需要活动信息固定参数
     */
    public Map<String, String> generateMap(String active,String verifyCode, boolean isAdd) {
        if (map == null) {
            map = new TreeMap<>();
        } else {
            map.clear();
        }
        requestBean.down_ = String.valueOf(System.currentTimeMillis());
        requestBean.down__ = String.valueOf(System.currentTimeMillis() + 123);
        requestBean.active = active;
        String deviceId = "86" + RandomUtils.generateNumString(13);
        requestBean.qid = MD5Utils.md5(deviceId);
        requestBean.mid = requestBean.qid;
        requestBean.type = type;
        requestBean.verify = MD5Utils.md5(verifyCode + requestBean.mid);
        map.put(Constant.ACTIVE, requestBean.active);
        map.put(Constant.DOWN_, requestBean.down_);
        map.put(Constant.QID, requestBean.mid);
        if (isAdd) {
            map.put(Constant.TYPE, requestBean.type);
            map.put(Constant.VERIFY, requestBean.verify);
        }
        return map;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setApi(ApiStore apiStore) {

        mApiStore = apiStore;
    }
}
