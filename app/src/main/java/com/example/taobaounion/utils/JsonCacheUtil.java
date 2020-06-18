package com.example.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.bean.CacheWithDuration;
import com.google.gson.Gson;

public class JsonCacheUtil {
    private static final JsonCacheUtil ourInstance = new JsonCacheUtil();
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    public static JsonCacheUtil getInstance() {
        return ourInstance;
    }

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";

    private JsonCacheUtil() {
        mSharedPreferences = BaseApplication.getContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String json = mGson.toJson(value);
        if (duration != -1)
            duration += System.currentTimeMillis();
//        保存一个有数据有时间的内容
        CacheWithDuration cacheWithDuration = new CacheWithDuration(json, duration);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
//        对时间进行判断
        long l = cacheWithDuration.getDuration() - System.currentTimeMillis();
        if (cacheWithDuration.getDuration() != -1 && l <= 0) {
//          时间过期了
            return null;
        } else {
//          没过期
            String cache = cacheWithDuration.getCache();
            return mGson.fromJson(cache, clazz);
        }
    }
}
