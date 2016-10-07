package com.kevin.lottery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private Parent rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("360活动抽奖集合");
        initRootLayout();
//        inflateLayout();
    }

    //给更布局填充内容
    private void inflateLayout() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("ui/main_ui.fxml"));
//            rootLayout.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载主要跟布局
    private void initRootLayout() {
        try {
            //加载跟布局
            rootLayout = FXMLLoader.load(getClass().getResource("ui/main_ui.fxml"));
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
}
