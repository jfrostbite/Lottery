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
import java.net.Proxy;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2016/10/5.
 * 单例设计模式
 * 内部静态类模式
 */
public class ApiStore {

    private static String drawer;
    private final Retrofit.Builder mReBuilder;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    public String cookie = "PHPSESSID=cn46udosre6c8s9ppmhm83mot5; netalliance_id=1.2.15161.; muuid=1487596739429_2635; mucid=15161.0011; muctm=; muctmr=eyJtaV91X3QiOiIyIiwibWlfdV92IjoiIiwibWlfdV9yIjoiaHR0cDovL3htdC53d3cubWkuY29tL2luZGV4LnBocD9pZD05MzgwXHUwMDI2c2NlbmU9MzciLCJtaV91X2QiOiIiLCJtaV90aWQiOiIifQ**; mutid=15161.00115XQB4efTrZfKhO677a4f170220211800; Hm_lvt_4982d57ea12df95a2b24715fb6440726=1487596740; Hm_lpvt_4982d57ea12df95a2b24715fb6440726=1487596740; lastsource=a.union.mi.com; mstz=||1816046639.1||http%3A%2F%2Fa.union.mi.com%2Fmua%3Fc%3D15161.0011|; mstuid=1487596740551_4790; xm_vistor=1487596740551_4790_1487596740554-1487596740554";
    private OkHttpClient.Builder mBuilder;

    private ApiStore() {

        //设置Okhttp
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                System.out.println(message);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mBuilder = new OkHttpClient.Builder()
//                .proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress("180.106.37.111", 8118)))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(generateRequestHeads());
//                .addInterceptor(generateRequest());
//                .addInterceptor(receiveCookie());
//                .cookieJar(generateCookie());
        //设置超市时间
        setRequestTime(mBuilder);

        okHttpClient = mBuilder.build();

        //配置Retrofit
        mReBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.HOST + drawer)
//                .baseUrl(Constant.HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = mReBuilder
                .build();
    }

    /**
     * 初始化需要的参数
     */
    public static ApiStore newInstance(String drawer) {
        ApiStore.drawer = drawer + "/";
        return ApiStoreHolder.INSTANCE;
    }

    /**
     * 设置代理连接
     */
    public ApiStore setProxy(Proxy proxy){
        okHttpClient = mBuilder.proxy(proxy).build();
        retrofit = mReBuilder.client(okHttpClient).build();
        return this;
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
        String model = Constant.MODEL_PHONE[new Random().nextInt(Constant.MODEL_PHONE.length - 1)];
//        String ua = "Dalvik/2.1.0 (Linux; U; Android 6.0; "+model+" Build/MRA58K)";
//        String ua = "Mozilla/5.0 (Linux; Android 4.4.2; HUAWEI GRA-CL10 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36;360appstore";
        String ua = "Mozilla/5.0 (Linux; Android 4.4.2; HUAWEI GRA-CL10 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36,gameunion";
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .header("X-Requested-With","com.qihoo.gameunion")
//                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Referer", "http://huodong.mobilem.360.cn//html/bgxxlshyk.html?360appstore=1&from=mp_0320bgxxx&webpg=pushngames_20190320_bgxxxshyk&os=19&vc=300070181&v=7.1.81&os_version=4.4.2&md=M353&sn=6.119186756728438&cpu=qualcomm+msm+8974+hammerhead+%28flattened+device+tree%29&ca1=armeabi-v7a&ca2=armeabi&m=e252016d8706956d67cda733c5c06683&m2=fb262c4d2a0aa2be27b734194b205680&ch=8969405&ppi=720_1280&startCount=1&re=1&tid=0&cpc=1&snt=-1&nt=1&gender=-1&age=0&newuser=1&theme=2&br=Xiaomi&carrier_id=0&s_3pk=1&ui_version=green&prepage=yxygj_20180205_60ka13m0&curpage=pushngames_20190320_bgxxxshyk")
                        .header("Accept-Encoding", "deflate")
                        .header("Accept-Language", "en-US")
//                        .header("Content-Type", "text/plain; charset=utf-8")
//                        .header("X-Channel", "webwww")
//                        .header("X-MajorVer", "5")
//                        .header("User-Agent","Mozilla/5.0 (Linux; Android 6.0.1; MI5) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36;360appstore")
                        .header("User-Agent", ua)
                        .header("Accept", "*/*")
                        .header("Cookie", "__guid=52855089.28830818364499212.1554448404124.3984")
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        };
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
