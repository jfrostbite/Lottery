package com.kevin.lottery.event;

import com.kevin.lottery.entity.LuckerBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiStore;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import rx.Observable;

import java.util.Map;

/**
 * 抽奖业务逻辑
 */
public class Controller {

    @FXML
    private TextField tf_name, tf_tel, tf_address, tf_base, tf_active, tf_code;
    @FXML
    private TextArea ta_content;
    @FXML
    private TextArea content;
    private ApiStore apiStore;
    private ApiService apiService;

    /**
     * 自动抽奖逻辑
     */
    @FXML
    private void startLottery() {
        /*Map<String, String> requestMap = getRequestMap(true);
        Observable<LuckerBean> observable = apiService.addChance(requestMap);
        observable.observeOn(Schedulers.io())
                .map((bean) -> {
                    String msg = "增加抽奖机会失败！";
                    if (Constant.STATUS_OK.equals(bean.status)) {
                        msg = "增加抽奖机会成功！";
                    }
                    return msg;
                })
                .subscribe((msg)->{
                    ta_content.appendText(msg+"\n");
                });*/
        Map<String, String> requestMap = getRequestMap(false);
        Observable<LuckerBean> observable = apiService.getChance(requestMap);
        observable.subscribe();
    }

    private Map<String, String> getRequestMap(boolean isAdd) {
        apiStore.setType("6");
        return apiStore.generateMap(tf_active.getText(), tf_code.getText(), isAdd);
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

}
