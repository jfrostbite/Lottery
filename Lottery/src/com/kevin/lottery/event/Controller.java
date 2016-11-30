package com.kevin.lottery.event;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kevin.lottery.draws.*;
import com.kevin.lottery.entity.DrawBean;
import com.kevin.lottery.helper.ThreadPoolHelper;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiStore;
import com.kevin.lottery.utils.GsonUtils;
import com.kevin.utils.TextUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 抽奖业务逻辑
 */
public class Controller implements OnDrawListener {

    public TextField tf_name, tf_tel, tf_address, tf_base, tf_active, tf_code;

    public Button btn_start, btn_add, btn_submit;

    public TableView tab_content;

    public TableColumn<DrawBean, String> tc_activityId, tc_verifyCode, tc_content;


    private ApiStore apiStore;
    private ApiService apiService;
    private boolean drawing;
    private String datas;
    private ObservableList<DrawBean> drawBeen;
    private ScheduledExecutorService pool;


    /**
     * 自动抽奖逻辑
     */
    private void startLottery() {

        if (!drawing) {

            for (int i = 0; i < drawBeen.size(); i++) {
                DrawTask task = new DrawTask(i);
                ThreadPoolHelper.newInstace().execute(task);
            }
        }
        btn_start.setText(drawing ? "开始" : "停止");
        drawing = !drawing;

    }

    private void start(Draw_Poco draw) {
        if (draw != null) {
            draw.setOnDrawListener(this);
            Map<String, String> requestMap = getRequestMap(draw);
            requestMap.put("uname",tf_name.getText().trim());
            requestMap.put("mobile",tf_tel.getText().trim());
            requestMap.put("addr",tf_address.getText().trim());
            draw.preDraw(requestMap).draw();
        } else {
            setContent(draw.getIndex(), "抽奖程序未启动...");
        }
    }

    private void start(Draw_Baby draw) {
        if (draw != null) {
            draw.setOnDrawListener(this);
            Map<String, String> requestMap = getRequestMap(draw);
            requestMap.put("phone",tf_tel.getText().trim());
            requestMap.put("name",tf_name.getText().trim());
            draw.preDraw(requestMap);
            requestMap = getRequestMap(draw);
            String code = apiStore.cookie.split(";")[0].split("=")[1];
            requestMap.put("code",code);
            draw.draw(requestMap);
        } else {
            setContent(draw.getIndex(), "抽奖程序未启动...");
        }
    }

    private void start(Draw_360 draw) {
        if (draw != null) {
            draw.setOnDrawListener(this);
            draw.preDraw(getRequestMap(draw));
        } else {
            setContent(draw.getIndex(), "抽奖程序未启动...");
        }
    }


    /**
     * 布局加载玩会自动执行
     */
    @FXML
    private void initialize() {
        pool = Executors.newScheduledThreadPool(10);

        init();
        initView();
        initData();
    }

    private void init() {
        apiStore = ApiStore.newInstance(tf_base.getText());
        apiService = apiStore.getService(ApiService.class);
        tf_code.setText("TUrnTaBlE7e75c76");
        tf_active.setText("7e75c7");

        File file = new File(System.getProperty("user.dir"), "360.txt");
        datas = TextUtils.file2String(file);
    }

    private void initData() {
        List<DrawBean> tmp = null;
        if (!TextUtils.isEmpty(datas)) {
            Gson gson = GsonUtils.getGson();
            tmp = gson.fromJson(datas, new TypeToken<ObservableList<DrawBean>>() {
            }.getType());
        } else {
            tmp = new ArrayList<>();
        }
        drawBeen = FXCollections.observableArrayList(tmp);
        if (drawBeen != null) {
            tab_content.setItems(drawBeen);
        }
    }

    private void initView() {

        tab_content.setEditable(true);
        tc_activityId.setEditable(true);
        tc_verifyCode.setEditable(true);

        tc_activityId.setCellValueFactory(new PropertyValueFactory<DrawBean, String>("activityId"));
        tc_verifyCode.setCellValueFactory(new PropertyValueFactory<DrawBean, String>("verifyCode"));
        tc_content.setCellValueFactory(new PropertyValueFactory<DrawBean, String>("content"));

        /**
         * 为没一列添加可编辑性
         */
        tc_activityId.setCellFactory(TextFieldTableCell.<DrawBean>forTableColumn());
        tc_activityId.setOnEditCommit(e -> {
            //从tabView中获取 填充表哥的对象
            ((DrawBean) e.getTableView().getItems().get(e.getTablePosition().getRow())).setActivityId(e.getNewValue());
        });

        tc_verifyCode.setCellFactory(TextFieldTableCell.<DrawBean>forTableColumn());
        tc_verifyCode.setOnEditCommit(e -> {
            ((DrawBean) e.getTableView().getItems().get(e.getTablePosition().getRow())).setVerifyCode(e.getNewValue());
        });

        btn_start.setOnAction(e -> {
            startLottery();
        });

        btn_submit.setOnAction(e -> {
            TreeMap<String, String> map = new TreeMap<>();
            map.put("ch", "interphoto_201612");
            map.put("uname",tf_name.getText().trim());
            map.put("mobile",tf_tel.getText().trim());
            map.put("addr",tf_address.getText().trim());
            map.put("winkey","09e3fc71a2bddefb1ce8a1efdf7fd2cf");
            map.put("record","8789");
            map.put("key","c6dc14bacad7cd9b828ac97ecde3d983");
            Draw_Poco poco = new Draw_Poco(0, apiService);
            poco.setOnDrawListener(this);
            poco.preDraw(map).submit(map);
        });

        btn_add.setOnAction(e -> {
            if (drawBeen != null) {
                boolean add = drawBeen.add(new DrawBean(tf_active.getText(), tf_code.getText(), ""));
                String str = GsonUtils.getGson().toJson(drawBeen);
                if (add) {
                    TextUtils.string2File(str, "360.txt", false);
                }
            }
        });
    }

    /**
     * 分配map
     *
     * @return
     */
    private synchronized Map<String, String> getRequestMap(Draw_360 draw) {
        draw.setType("6");
        return draw.generateMap(drawBeen.get(draw.getIndex()).getActivityId(), drawBeen.get(draw.getIndex()).getVerifyCode(), true);
    }

    private synchronized Map<String, String> getRequestMap(Draw_Poco draw) {
        return draw.generateMap();
    }

    private synchronized Map<String, String> getRequestMap(Draw_Baby draw) {
        return draw.generateMap();
    }

    private void showDialog(String drawmark) {
        Platform.runLater(() -> {
            btn_start.setText("开始");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("恭喜获得：");
            alert.setContentText(drawmark);
            alert.setOnCloseRequest(e -> {
                System.out.println("guanbi le ");
            });
            alert.show();
        });
    }

    /**
     * 设置文本内容统一数据
     *
     * @param content
     */
    private void setContent(int index, String content) {
        Platform.runLater(() -> {
            drawBeen.get(index).setContent(TextUtils.getCurrentTime() + content);
//            System.out.println(content);
        });
    }

    @Override
    public void onDraw(int index, String str) {
        setContent(index, str);
    }

    @Override
    public void alertDialog(String... str) {
        drawing = false;
        showDialog(str[0]);
    }

    @Override
    public void saveLog(String... str) {
        TextUtils.string2File(str[0] + "\n"+str[1]+"\n", "draw.txt", true);
    }

    class DrawTask extends Task<Void> {

        private int index;

        public DrawTask(int index) {

            this.index = index;
        }

        @Override
        protected Void call() throws Exception {
//            Draw_360 draw_360 = new Draw_360(apiService);
//            draw_360.setIndex(index);
//            Draw_Poco draw = new Draw_Poco(index, apiService);
            Draw_Baby draw = new Draw_Baby(index, apiService);
            do {
                if (!drawing) {
                    drawing = false;
                    break;
                }
                start(draw);
//                start(draw_360);
                Thread.sleep(100);
            } while (drawing);
            return null;
        }
    }
}
