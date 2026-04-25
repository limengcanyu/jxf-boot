package org.asura.flaw.utils;

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
