package com.hcll.fishshrimpcrab.common.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by hong on 2018/2/10.
 */

public class JsonUtils {
    @NonNull
    public static RequestBody createJsonRequestBody(Map<String, ? extends Object> map) {
        JSONObject jsonObj = new JSONObject(map);
        return RequestBody.create(MediaType.parse("Content-Type,application/json"), jsonObj.toString());
    }

    @NonNull
    public static RequestBody createJsonRequestBody(Object o) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return RequestBody.create(MediaType.parse("Content-Type,application/json"), json);
    }
}
