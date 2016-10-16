package com.kevin.lottery.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by kevin on 2016/10/16.
 */
public class DrawBean {

    private SimpleStringProperty activityId;
    private SimpleStringProperty verifyCode;
    private SimpleStringProperty content;

    public DrawBean(){
        this("","","");
    }

    public DrawBean(String activityId, String verifyCode, String content) {

        this.activityId = new SimpleStringProperty(activityId);
        this.verifyCode = new SimpleStringProperty(verifyCode);
        this.content = new SimpleStringProperty(content);

    }

    public String getActivityId() {
        return activityId.get();
    }

    public SimpleStringProperty activityIdProperty() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId.set(activityId);
    }

    public String getVerifyCode() {
        return verifyCode.get();
    }

    public SimpleStringProperty verifyCodeProperty() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode.set(verifyCode);
    }

    public String getContent() {
        return content.get();
    }

    public SimpleStringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }
}
