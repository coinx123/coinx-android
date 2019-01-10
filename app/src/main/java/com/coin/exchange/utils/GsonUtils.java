package com.coin.exchange.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/6/7
 * @description Gson 工具类
 */

public class GsonUtils {

    private static GsonUtils mInstance;
    private Gson mGson;

    private GsonUtils() {
//        mGson = new Gson();
        mGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static GsonUtils getInstance() {
        if (mInstance == null) {
            synchronized (GsonUtils.class) {
                if (mInstance == null) {
                    mInstance = new GsonUtils();
                }
            }
        }
        return mInstance;
    }

    public static Gson getGson() {
        return getInstance().mGson;
    }


    /**
     * 将对象转为JSON串，此方法能够满足大部分需求
     *
     * @param src :将要被转化的对象
     * @return :转化后的JSON串
     */
    public String toJson(Object src) {
        if (src == null) {
            return mGson.toJson(JsonNull.INSTANCE);
        }
        return mGson.toJson(src);
    }

    /**
     * 用来将JSON串转为对象，但此方法不可用来转带泛型的集合
     *
     * @param json     json串
     * @param classOfT 需要转换的类class
     * @return
     */
    public <T> Object fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, (Type) classOfT);
    }

    /**
     * 用来将 JSON串 转为对象，此方法可用来转带泛型的集合，
     * 如：Type 为 new TypeToken<List<T>>(){}.getType()，
     * 其它类也可以用此方法调用，就是将List<T>替换为你想要转成的类
     *
     * @param json    json串
     * @param typeOfT 需要转换的类class
     * @return
     */
    public Object fromJson(String json, Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }

    /**
     * 用来将 JSON串 转为对象，此方法可用来转带泛型的集合
     * 如：Type为 new TypeToken<List<T>>(){}.getType()其它类也可以用此方法调用，
     * 就是将List<T>替换为你想要转成的类
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public List<Object> fromJsonArray(String json, Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }

    /**
     * 将 json字符串 的key->value转为 HashMap
     *
     * @param jsonStr json字符串
     * @return
     */
    public static HashMap<String, String> jsonToMap(String jsonStr) {
        HashMap<String, String> map = new HashMap<>();

        if (TextUtils.isEmpty(jsonStr)) {
            return new HashMap<>();
        }

        try {
            // 将 json 转为 JSONObject
            JSONObject obj = new JSONObject(jsonStr);

            // 获取所以key值
            Iterator<?> strKeys = obj.keys();
            //遍历JSONObject
            while (strKeys.hasNext()) {
                String key = strKeys.next().toString();
                String value = obj.getString(key);
                if (value == null || value.equals("null")) {
                    value = "";
                }
                map.put(key, value);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    /**
     * 将 json字符串 的key->value转为 List<HashMap>
     *
     * @param jsonStr json字符串
     * @return
     */
    public static ArrayList<HashMap<String, String>> jsonToList(String jsonStr) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(jsonStr)) {
            return arrayList;
        }

        try {
            // 将 json 转为 jsonArray
            JSONArray jsonArray = new JSONArray(jsonStr);

            // 开始遍历
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();

                Iterator<?> strKeys = obj.keys();
                while (strKeys.hasNext()) {
                    String key = strKeys.next().toString();
                    String value = obj.getString(key);
                    if (value == null || value.equals("null")) {
                        value = "";
                    }
                    map.put(key, value);
                }

                arrayList.add(map);
            }

            return arrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
