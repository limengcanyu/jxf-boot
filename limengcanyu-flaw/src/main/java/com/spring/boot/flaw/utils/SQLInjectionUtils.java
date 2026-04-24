package com.spring.boot.flaw.utils;

public class SQLInjectionUtils {
    public static String replaceIllegalCharacter(String queryParam){
        queryParam = queryParam.replaceAll("%", "")
                .replaceAll("and", "")
                .replaceAll("&&", "")
                .replaceAll("or", "")
                .replaceAll("||", "")
                .replaceAll("=", "");
        return queryParam;
    }
}
