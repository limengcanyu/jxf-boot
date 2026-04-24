package com.spring.boot.data.elasticsearch.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 11:24
 */
public class LocalDateTimeUtils {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime localDateTime){
        return localDateTime.format(formatter);
    }

    public static LocalDateTime parse(String localDateTime){
        return LocalDateTime.parse(localDateTime, formatter);
    }
}
