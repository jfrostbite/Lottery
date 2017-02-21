package com.kevin.lottery.http;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
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
    public String cookie = "PHPSESSID=cn46udosre6c8s9ppmhm83mot5; netalliance_id=1.2.15161.; muuid=1487596739429_2635; mucid=15161.0011; muctm=; muctmr=eyJtaV91X3QiOiIyIiwibWlfdV92IjoiIiwibWlfdV9yIjoiaHR0cDovL3htdC53d3cubWkuY29tL2luZGV4LnBocD9pZD05MzgwXHUwMDI2c2NlbmU9MzciLCJtaV91X2QiOiIiLCJtaV90aWQiOiIifQ**; mutid=15161.00115XQB4efTrZfKhO677a4f170220211800; Hm_lvt_4982d57ea12df95a2b24715fb6440726=1487596740; Hm_lpvt_4982d57ea12df95a2b24715fb6440726=1487596740; lastsource=a.union.mi.com; mstz=||1816046639.1||http%3A%2F%2Fa.union.mi.com%2Fmua%3Fc%3D15161.0011|; mstuid=1487596740551_4790; xm_vistor=1487596740551_4790_1487596740554-1487596740554";

    private ApiStore() {

        //设置Okhttp
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                System.out.println(message);
            }
        };
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(logger);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(generateRequestHeads());
//                .addInterceptor(generateRequest());
//                .addInterceptor(receiveCookie());
//                .cookieJar(generateCookie());
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
     * 通用请求头Head
     * <p>
     * okhttp的拦截器利用chain 获取Resquest 利用Request 设置新的Request，利用Request获取Response
     * 在这个过程中完成了，对请求的设置，拦截操作
     */
    private Interceptor generateRequestHeads() {
        Interceptor requestHead = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
//                        .header("X-Requested-With","com.qihoo.appstore")
                        .header("X-Requested-With", "XMLHttpRequest")
//                        .header("User-Agent","Mozilla/5.0 (Linux; Android 6.0.1; MI5) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36;360appstore")
                        .header("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.1; MI NOTE LTE Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043024 Safari/537.36 MicroMessenger/6.5.4.1000 NetType/WIFI Language/zh_CN")
                        .header("Cookie", cookie)
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
        Interceptor requestHead = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //首先重建HttpUrl，添加通用参数
                HttpUrl httpUrl = chain.request()
                        .url()
                        .newBuilder()
//                        .addQueryParameter(Constant.V, String.valueOf(Instant.now().getEpochSecond()))
                        .addQueryParameter(Constant.DOWN_, String.valueOf(System.currentTimeMillis()))
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
     *
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
    private CookieJar generateCookie() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
        JavaNetCookieJar javaNetCookieJar = new JavaNetCookieJar(cookieManager);
        return javaNetCookieJar;
    }

    /**
     * 自定义获取Cookie拦截器
     */
    private Interceptor receiveCookie() {
        return chain -> {
            Request request = chain.request();
            Response respose = chain.proceed(request);
            if ("GET".equals(request.method())) {
                if (!respose.headers("Set-Cookie").isEmpty()) {
                    Observable.from(respose.headers("Set-Cookie"))
                            .map(v -> {
                                cookie = "";
                                return v.split(";")[0];
                            }).subscribe(v -> {
                        cookie += v + ";";
                    });
                }
            } else {
            }
            request = request.newBuilder().addHeader("Cookie", ApiStore.this.cookie).build();
            return chain.proceed(request);
        };
    }
}
