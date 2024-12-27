package com.devteria.identity_service.utils;

public class StringUtils {
    public static boolean checkString(String username){
        return username!=null && !username.isEmpty();
    }
}
