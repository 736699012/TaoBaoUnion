package com.example.taobaounion.utils;

import android.text.TextUtils;

public class UrlUtil {

    public static String getCoverUrl(String baseUrl, int size) {

        if (baseUrl.startsWith("http") || baseUrl.startsWith("https")) {
            return baseUrl;
        } else {
            return Constant.IMG_BASE_URL + baseUrl + "_" + size + "x" + size + ".jpg";
        }
    }

    public static String getCoverUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl))
            return "";
        if(baseUrl.startsWith("https:https:")){
            return baseUrl.substring(6);
        }
        if (baseUrl.startsWith("http") || baseUrl.startsWith("https")) {
            return baseUrl;
        } else {
            return Constant.IMG_BASE_URL + baseUrl ;
        }
    }

    public static String getTicketUrl(String baseUrl) {
        if (baseUrl.startsWith("http") || baseUrl.startsWith("https")) {
            return baseUrl;
        } else {
            return Constant.IMG_BASE_URL+baseUrl;
        }
    }
}
