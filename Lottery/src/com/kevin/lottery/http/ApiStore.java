package com.kevin.lottery.http;

import com.kevin.lottery.entity.RequestBean;
import com.kevin.lottery.utils.MD5Utils;
import com.kevin.utils.RandomUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kevin on 2016/10/5.
 * 单例设计模式
 * 内部静态类模式
 */
public class ApiStore {

    private static String drawer;
    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;
    private final RequestBean requestBean;
    private String type;
    private TreeMap<String, String> map;

    private ApiStore() {
        //初始化请求参数
        requestBean = new RequestBean();
        //设置Okhttp
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        //配置Retrofit
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.HOST + drawer)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 初始化需要的参数
     */
    public static ApiStore newInstance(String drawer) {
        ApiStore.drawer = drawer + "/";
        return ApiStoreHolder.INSTANCE;
    }

    private static class ApiStoreHolder {
        private static final ApiStore INSTANCE = new ApiStore();
    }

    /**
     * 获取ApiService 用于请求网络
     */
    public <T> T getService(Class<T> cls) {
        return retrofit.create(cls);
    }

    /**
     * 获取通用参数,需要活动信息固定参数
     */
    public Map<String, String> generateMap(String active,String verifyCode, boolean isAdd) {
        if (map == null) {
            map = new TreeMap<>();
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
        map.put(isAdd ? Constant.QID : Constant.QID, requestBean.mid);
        if (isAdd) {
            map.put(Constant.TYPE, requestBean.type);
            map.put(Constant.VERIFY, requestBean.verify);
            map.put(Constant.DOWN__, requestBean.down__);
        }
        return map;
    }

    /**
     * 设置获取抽奖次数的类型参数
     */
    public void setType(String type) {
        this.type = type;
    }
}
