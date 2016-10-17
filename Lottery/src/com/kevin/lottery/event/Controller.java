package com.kevin.lottery.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kevin.lottery.draws.Draw_360;
import com.kevin.lottery.draws.OnDrawListener;
import com.kevin.lottery.entity.DrawBean;
import com.kevin.lottery.http.ApiService;
import com.kevin.lottery.http.ApiStore;
import com.kevin.utils.TextUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 抽奖业务逻辑
 */
public class Controller implements OnDrawListener {

    public TextField tf_name, tf_tel, tf_address, tf_base, tf_active, tf_code;

    public Button btn_start,btn_add,btn_submit;

    public TableView tab_content;

    public TableColumn<DrawBean,String> tc_activityId,tc_verifyCode,tc_content;


    private ApiStore apiStore;
    private ApiService apiService;
    private boolean drawing;
    private Task task;
    private String datas;
    private ObservableList<DrawBean> drawBeen;
    private int i = 0;


    /**
     * 自动抽奖逻辑
     */
    private void startLottery() {
        task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                Draw_360 draw_360 = new Draw_360(apiService);
                draw_360.setIndex(i++);
                while (drawing) {
                    if (isCancelled()) {
                        drawing = false;
                        break;
                    }
                    start(draw_360);
                    Thread.sleep(100);
                }
                return null;
            }
        };

        Service<Void> service = new Service<Void>(){
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Void call() throws Exception {
                        Draw_360 draw_360 = new Draw_360(apiService);
                        draw_360.setIndex(i++);
                        while (drawing) {
                            if (isCancelled()) {
                                drawing = false;
                                break;
                            }
                            Controller.this.start(draw_360);
                            Thread.sleep(100);
                        }
                        return null;
                    }
                };
            }
        };

        if (!drawing) {
            for (int j = 0; j < 2 ; j++) {
                Thread thread = new Thread(new Task<Void>(){
                    @Override
                    protected Void call() throws Exception {
                        Draw_360 draw_360 = new Draw_360(apiService);
                        draw_360.setIndex(i++);
                        while (drawing) {
                            if (isCancelled()) {
                                drawing = false;
                                break;
                            }
                            start(draw_360);
                            Thread.sleep(100);
                        }
                        return null;
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
//                service.start();
        } else {
            if (task.isRunning()) {
                boolean cancel = task.cancel();
            }
        }
        btn_start.setText(drawing ? "开始" : "停止");
        drawing = !drawing;
    }

    private void start(Draw_360 draw_360) {
        if (draw_360 != null) {
            draw_360.setOnDrawListener(this);
            draw_360.preDraw(getRequestMap(draw_360.getIndex()));
        } else {
            setContent(draw_360.getIndex(),"抽奖程序未启动...");
        }
    }


    /**
     * 布局加载玩会自动执行
     */
    @FXML
    private void initialize() {
        init();
        initView();
        initData();

    }

    private void init() {
        apiStore = ApiStore.newInstance(tf_base.getText());
        apiService = apiStore.getService(ApiService.class);
        tf_code.setText("TUrnTaBlE7e75c76");
        tf_active.setText("7e75c7");

        File file = new File("/Users/kevin/Desktop/360.txt");
        datas = TextUtils.file2String(file);
    }

    private void initData() {

        if (!TextUtils.isEmpty(datas)) {
            Gson gson = getGson();
            List<DrawBean> tmp = gson.fromJson(datas, new TypeToken<ObservableList<DrawBean>>() {
            }.getType());
            drawBeen = FXCollections.observableArrayList(tmp);
        }
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
        tc_activityId.setOnEditCommit(e ->{
            //从tabView中获取 填充表哥的对象
            ((DrawBean)e.getTableView().getItems().get(e.getTablePosition().getRow())).setActivityId(e.getNewValue());
        });

        tc_verifyCode.setCellFactory(TextFieldTableCell.<DrawBean>forTableColumn());
        tc_verifyCode.setOnEditCommit(e ->{
            ((DrawBean)e.getTableView().getItems().get(e.getTablePosition().getRow())).setVerifyCode(e.getNewValue());
        });

        btn_start.setOnAction(e->{
            startLottery();
        });
        btn_submit.setOnAction(e ->{
        });
        btn_add.setOnAction(e ->{
            if (drawBeen != null) {
                boolean add = drawBeen.add(new DrawBean(tf_active.getText(), tf_code.getText(), ""));
                String str = getGson().toJson(drawBeen);
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
    private Map<String, String> getRequestMap(int index) {
        apiStore.setType("6");
        return apiStore.generateMap(drawBeen.get(index).getActivityId(), drawBeen.get(index).getVerifyCode(), true);
    }

    private void showDialog(String drawmark) {
        Platform.runLater(()->{
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
    private void setContent(int index,String content) {
        Platform.runLater(()->{
//            ta_content.setText(TextUtils.getCurrentTime() + content + "\n");
            drawBeen.get(index).setContent(content);
//            System.out.println(content);
        });
    }

    @Override
    public void onDraw(int index,String str) {
        setContent(index,str);
    }

    @Override
    public void alertDialog(String str) {
        if (task.isRunning()) {
            task.cancel();
        }
        showDialog(str);
    }

    /**
     * 根据需求自定义gson解析器，用于解析ObservableList集合
     * @return
     */
    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(DrawBean.class, new TypeAdapter<DrawBean>() {
            @Override
            public void write(JsonWriter jsonWriter, DrawBean drawBeen) throws IOException {
                jsonWriter.beginObject()
                        .name("activityId").value(drawBeen.getActivityId())
                        .name("verifyCode").value(drawBeen.getVerifyCode())
                        .name("prizeName").value(drawBeen.getContent())
                        .endObject();
            }

            @Override
            public DrawBean read(JsonReader jsonReader) throws IOException {
                DrawBean drawBean = new DrawBean();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()) {
                        case "activityId":
                            drawBean.setActivityId(jsonReader.nextString());
                            break;
                        case "verifyCode":
                            drawBean.setVerifyCode(jsonReader.nextString());
                            break;
                        case "prizeName":
                            drawBean.setContent(jsonReader.nextString());
                            break;
                    }
                }
                jsonReader.endObject();
                return drawBean;
            }
        }).create();
    }
}
