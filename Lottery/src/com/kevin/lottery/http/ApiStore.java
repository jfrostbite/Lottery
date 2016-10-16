package com.kevin.lottery.http;

import com.kevin.lottery.entity.RequestBean;
import com.kevin.lottery.utils.MD5Utils;
import com.kevin.utils.RandomUtils;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

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
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger(){
            @Override
            public void log(String message) {
                System.out.println(message);
            }
        };
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(logger);
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(generateRequestHeads())
                .addInterceptor(generateRequest())
                .cookieJar(generateCookie());
        //设置超市时间
        setRequestTime(builder);

        okHttpClient = builder.build();

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
//        map.put(Constant.DOWN_, requestBean.down_);
        map.put(Constant.QID, requestBean.mid);
        if (isAdd) {
            map.put(Constant.TYPE, requestBean.type);
            map.put(Constant.VERIFY, requestBean.verify);
        }
        return map;
    }

    /**
     * 设置获取抽奖次数的类型参数
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 通用请求头Head
     *
     * okhttp的拦截器利用chain 获取Resquest 利用Request 设置新的Request，利用Request获取Response
     * 在这个过程中完成了，对请求的设置，拦截操作
     */
    private Interceptor generateRequestHeads() {
        Interceptor requestHead = new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .header("X-Requested-With","com.qihoo.appstore")
                        .header("User-Agent","Mozilla/5.0 (Linux; Android 6.0.1; MI NOTE LTE Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36;360appstore")
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        };
        return requestHead;
    }

    /**
     * 设置通用请求参数
     */
    private Interceptor generateRequest() {
        Interceptor requestHead = new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                //首先重建HttpUrl，添加通用参数
                HttpUrl httpUrl = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter(Constant.DOWN_, String.valueOf(Instant.now().getEpochSecond()))
                        .build();
                //重建Request，添加重建后的HttpUrl
                Request request = chain.request()
                        .newBuilder()
                        .url(httpUrl)
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        };
        return requestHead;
    }

    /**
     * 设置超时时间
     * @param builder
     */
    private void setRequestTime(OkHttpClient.Builder builder) {
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);
    }

    /**
     * 设置cookie的非持久化
     */
    private CookieJar generateCookie () {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar javaNetCookieJar = new JavaNetCookieJar(cookieManager);
        return javaNetCookieJar;
    }
}
