package com.kevin.lottery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private File file;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("360活动抽奖集合");
        initRootLayout();
        loadProperties();
        inflateLayout();
    }

    private void loadProperties() {
        String property = System.getProperties().getProperty("user.dir");
        file = new File(property,"360.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            properties.list(System.out);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("文件读写失败！");
            alert.show();
        }
    }

    //给更布局填充内容
    private void inflateLayout() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("main_ui.fxml"));
            rootLayout.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载主要跟布局
    private void initRootLayout() {
        try {
            //加载跟布局
            rootLayout = FXMLLoader.load(getClass().getResource("root_ui.fxml"));
            //创建场景
            Scene scene = new Scene(rootLayout);
            //给舞台设置场景
            primaryStage.setScene(scene);
            //固定窗口大小
            primaryStage.setResizable(false);
            //展示舞台
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
