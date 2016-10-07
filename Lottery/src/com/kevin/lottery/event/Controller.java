package com.kevin.lottery.event;

import com.google.gson.Gson;
import com.kevin.lottery.entity.BaseBean;
import com.kevin.lottery.entity.LotteryBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiStore;
import com.kevin.lottery.http.ApiSubscriber;
import com.kevin.lottery.http.Constant;
import com.kevin.utils.TextUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.time.Instant;
import java.util.Map;

/**
 * 抽奖业务逻辑
 */
public class Controller {

    @FXML
    private TextField tf_name, tf_tel, tf_address, tf_base, tf_active, tf_code;
    @FXML
    private Button btn_start;
    @FXML
    private TextArea ta_content;
    private ApiStore apiStore;
    private ApiService apiService;
    private boolean drawing;
    private Task task;

    /**
     * 自动抽奖逻辑
     */
    @FXML
    private void startLottery() {
        task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                while (drawing) {
                    if (isCancelled()) {
                        drawing = !drawing;
                        break;
                    }
                    start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (isCancelled()) {
                            drawing = !drawing;
                            break;
                        }
                    }
                }
                return null;
            }
        };
        if (!drawing) {
            new Thread(task).start();
        } else {
            if (task.isRunning()) {
                task.cancel();
            }
        }
        btn_start.setText(drawing ? "开始" : "停止");
        drawing = !drawing;
    }

    private void start() {
        try {
            Map<String, String> requestMap = getRequestMap();
            addChance(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        setContent(s);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        setContent("错误：" + code + "；" + msg);
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
                        setContent(s + "\n");
                        chance = Integer.parseInt(s);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        setContent("错误：" + code + "；" + msg);
                    }

                    @Override
                    public void onFinish() {
                        startDraw(requestMap);
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
                            setContent(remove+";恭喜获得：" + lotteryBean.drawmark);
                            Platform.runLater(()->{
                                if (task.isRunning()) {
                                    task.cancel();
                                }
                            });
                            showDialog(lotteryBean.drawmark);
                        } else {
                            setContent("很遗憾，未中奖！>>> " +lotteryBean.drawmark);
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        setContent(msg);

                    }

                    @Override
                    public void onFinish() {
                        try {
                            Thread.sleep(100);
                            if (drawing) {
//                                start();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        System.gc();
                    }
                });
    }

    /**
     * 分配map
     *
     * @return
     */
    private Map<String, String> getRequestMap() {
        apiStore.setType("6");
        return apiStore.generateMap(tf_active.getText(), tf_code.getText(), true);
    }

    /**
     * 布局加载玩会自动执行
     */
    @FXML
    private void initialize() {
        apiStore = ApiStore.newInstance(tf_base.getText());
        apiService = apiStore.getService(ApiService.class);
    }

    /**
     * 提交信息逻辑
     */
    @FXML
    private void submitInfo() {
        String name = tf_name.getText();
        String tel = tf_tel.getText();
        String add = tf_address.getText();
    }

    private void showDialog(String drawmark) {
        Platform.runLater(()->{
            btn_start.setText("开始");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("恭喜获得：");
            alert.setContentText(drawmark);
            alert.show();
        });
    }

    /**
     * 设置文本内容统一数据
     *
     * @param content
     */
    private void setContent(String content) {
        Platform.runLater(()->{
            ta_content.setText(TextUtils.getCurrentTime() + content + "\n");
        });
    }
}
