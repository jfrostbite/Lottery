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
    private String deviceId;

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
        map.putAll(generateMap(requestBean.active, requestBean.verify, deviceId, false));
        submitInfo(map);
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
                String string = str.string();
                if (string.contains("jQuery")) {
                    string = string.substring(string.indexOf("({") + 1, string.indexOf("})"));
                    string = string + "}";
                }
                bean = new Gson().fromJson(string, new TypeToken<BaseBean<String>>() {
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
                        setProxy(true);
                        if (mListener!=null) {
                            mListener.onDraw(index,"错误：" + code + "；" + msg);
                        }
                    }

                    @Override
                    public void onFinish() {
                        getChance(generateMap(requestBean.active, requestBean.verify, deviceId, false));

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
                String string = str.string();
                if (string.contains("jQuery")) {
                    string = string.substring(string.indexOf("({") + 1, string.indexOf("})"));
                    string = string + "}";
                }
                bean = new Gson().fromJson(string, new TypeToken<BaseBean<String>>() {
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
                        setProxy(true);
                        if (mListener!=null) {
                            mListener.onDraw(index,"错误：" + code + "；" + msg);
                        }
                    }

                    @Override
                    public void onFinish() {
                        for (int i = 0; i < chance; i++) {
                            startDraw(generateMap(requestBean.active, requestBean.verify, deviceId, false));
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
                                mListener.alertDialog(lotteryBean.drawmark,mid);
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
                        setProxy(true);
                        if (mListener!=null) {
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
                mListener.title(isa.toString());
                apiService = mApiStore.getService(ApiService.class);
                iterator.remove();
            }
        }
    }


    /**
     * 提交信息逻辑
     */
    private void submitInfo(Map<String, String> requestMap) {
        String remove = requestMap.remove(Constant.QID);
        if (remove != null) {
            requestMap.put(Constant.MID, remove);
        }
        String mid = requestMap.get(Constant.MID);
        requestMap.put(Constant.SENDTIME, "1");

        apiService.sendInfo(requestMap)
                .map(str -> {
                    BaseBean<String> bean = null;
                    try {
                        String string = str.string();
                        if (string.contains("jQuery")) {
                            string = string.substring(string.indexOf("({"), string.indexOf("})"));
                            string = "{" + string + "}";
                        }
                        bean = new Gson().fromJson(string, new TypeToken<BaseBean<String>>() {
                        }.getType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bean;
                })
                .subscribe(new ApiSubscriber<BaseBean<String>>() {
                    @Override
                    public void onSuccess(BaseBean<String> bean) {
                        if (Constant.STATUS_OK.equals(bean.status) || "success".equals(bean.data)) {
                            mListener.alertDialog("保存成功");
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 获取通用参数,需要活动信息固定参数
     */
    public Map<String, String> generateMap(String active, String verifyCode, String deviceId, boolean isAdd) {
        this.deviceId = deviceId;
        if (map == null) {
            map = new TreeMap<>();
        } else {
            map.clear();
        }
        long time = System.currentTimeMillis();
        String jscb = "jQuery2240"+RandomUtils.generateNumString(17)+"_" + (time);
        requestBean.down_ = String.valueOf(time + 2);
        requestBean.down__ = String.valueOf(time + 1234);
        requestBean.active = active;
        requestBean.qid = deviceId;
        requestBean.mid = requestBean.qid;
        requestBean.type = type;
        requestBean.verify = MD5Utils.md5(verifyCode + requestBean.mid);
        map.put(Constant.ACTIVE, requestBean.active);
        map.put(Constant.DOWN_, requestBean.down_);
        map.put(Constant.DOWN__, requestBean.down__);
        map.put(Constant.MID, requestBean.mid);
        map.put(Constant.JSCALL, jscb);
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
