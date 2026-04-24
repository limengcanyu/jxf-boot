package com.spring.boot.example;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Instant;

/**
 * ULID生成工具类
 * 默认使用中国时区（Asia/Shanghai），同时支持指定其他时区
 */
public class UlidGenerator {

    /**
     * 默认时区：中国时区（Asia/Shanghai，UTC+8）
     */
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

    /**
     * 生成当前时间的ULID（使用默认时区）
     */
    public static String generate() {
        return generate(LocalDateTime.now(DEFAULT_ZONE), DEFAULT_ZONE);
    }

    /**
     * 基于指定时间生成ULID（使用默认时区）
     * @param localDateTime 基于默认时区的本地时间
     */
    public static String generate(LocalDateTime localDateTime) {
        return generate(localDateTime, DEFAULT_ZONE);
    }

    /**
     * 基于指定时间和时区生成ULID
     * @param localDateTime 本地时间（需配合时区使用）
     * @param zoneId 时区（如ZoneId.of("UTC")、ZoneId.of("America/New_York")等）
     */
    public static String generate(LocalDateTime localDateTime, ZoneId zoneId) {
        // 将本地时间转换为UTC时间戳（ULID要求基于UTC时间戳）
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        long timestampMs = instant.toEpochMilli();

        return UlidCreator.getUlid(timestampMs).toString();
    }

    /**
     * 解析ULID的生成时间（转换为默认时区的LocalDateTime）
     */
    public static LocalDateTime parse(String ulid) {
        return parse(ulid, DEFAULT_ZONE);
    }

    /**
     * 解析ULID的生成时间（转换为指定时区的LocalDateTime）
     */
    public static LocalDateTime parse(String ulid, ZoneId zoneId) {
        Ulid ulidObj = Ulid.from(ulid);
        Instant instant = Instant.ofEpochMilli(ulidObj.getTime());
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * 验证ULID格式是否合法
     */
    public static boolean isValid(String ulid) {
        try {
            Ulid.from(ulid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    public static void main(String[] args) {
        // 1. 使用默认中国时区生成ULID（最常用）
        String defaultUlid = UlidGenerator.generate();
        System.out.println("默认中国时区ULID：" + defaultUlid);
        LocalDateTime defaultTime = UlidGenerator.parse(defaultUlid);
        System.out.println("解析为中国时间：" + defaultTime);

        // 2. 基于指定中国时间生成ULID
        LocalDateTime chinaTime = LocalDateTime.of(2024, 1, 1, 8, 0, 0);
        String chinaUlid = UlidGenerator.generate(chinaTime);
        System.out.println("指定中国时间生成的ULID：" + chinaUlid);

        // 3. 使用UTC时区生成ULID
        LocalDateTime utcTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        String utcUlid = UlidGenerator.generate(utcTime, ZoneId.of("UTC"));
        System.out.println("UTC时区生成的ULID：" + utcUlid);

        // 4. 将ULID解析为纽约时间
        LocalDateTime newYorkTime = UlidGenerator.parse(utcUlid, ZoneId.of("America/New_York"));
        System.out.println("解析为纽约时间：" + newYorkTime);
    }

}

