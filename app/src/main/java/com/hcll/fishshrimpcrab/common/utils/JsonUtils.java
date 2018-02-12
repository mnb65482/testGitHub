package com.hcll.fishshrimpcrab.common.utils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by hong on 2018/2/10.
 */

public class JsonUtils {
    public static RequestBody createJsonRequestBody(Map<String, ? extends Object> map) {
        JSONObject jsonObj = new JSONObject(map);
        return RequestBody.create(MediaType.parse("Content-Type,application/json"), jsonObj.toString());
    }
}
