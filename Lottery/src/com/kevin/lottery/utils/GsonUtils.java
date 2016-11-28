package com.kevin.lottery.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kevin.lottery.entity.DrawBean;

import java.io.IOException;

/**
 * Created by kevin on 2016/11/27.
 */
public class GsonUtils {

    /**
     * 根据需求自定义gson解析器，用于解析ObservableList集合
     *
     * @return
     */
    public static Gson getGson() {
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
