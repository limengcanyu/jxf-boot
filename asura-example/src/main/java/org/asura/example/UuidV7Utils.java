package org.asura.example;

import com.fasterxml.uuid.Generators;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.time.Instant;

public class UuidV7Utils {

    /**
     * 生成 UUID v7（基于当前毫秒时间戳，有序）
     * 格式：36 字符（含连字符），例如：018e9a6b-1c3d-7e8f-90ab-cdef01234567
     */
    public static UUID generate() {
        // timeBasedEpochGenerator 专门用于生成 UUID v7
        return Generators.timeBasedEpochGenerator().generate();
    }

    /**
     * 生成 UUID v7 的字符串形式
     */
    public static String generateString() {
        return generate().toString();
    }

    /**
     * 从 UUID v7 中解析生成时间（毫秒级时间戳，UTC）
     * @param uuid 必须是 UUID v7，否则解析结果无效
     */
    public static long parseTimestamp(UUID uuid) {
        // UUID v7 前 48 位为毫秒时间戳（自 1970-01-01 UTC 起）
        // 高位（mostSignificantBits）共 64 位，前 48 位为时间戳，需右移 16 位提取
        long timestampBits = (uuid.getMostSignificantBits() >>> 16) & 0x0000FFFFFFFFFFFFL;
        return timestampBits;
    }

    /**
     * 从 UUID v7 中解析生成时间（Instant 类型，UTC）
     */
    public static Instant parseInstant(UUID uuid) {
        return Instant.ofEpochMilli(parseTimestamp(uuid));
    }

    /**
     * 验证 UUID 是否为 v7 版本
     * UUID 版本通过高位第 6 个字节的前 4 位标识（v7 对应 0111）
     */
    public static boolean isVersion7(UUID uuid) {
        long version = (uuid.getMostSignificantBits() >>> 12) & 0x0F;
        return version == 7;
    }

    /**
     * 验证 UUID 字符串格式是否合法且为 v7 版本
     */
    public static boolean isValidV7String(String uuidStr) {
        try {
            UUID uuid = UUID.fromString(uuidStr);
            return isVersion7(uuid);
        } catch (IllegalArgumentException e) {
            return false; // 格式错误
        }
    }

    public static void main(String[] args) {
        // 生成 UUID v7 对象
        UUID uuid = UuidV7Utils.generate();
        System.out.println("UUID v7 对象: " + uuid);

        // 生成 UUID v7 字符串
        String uuidStr = UuidV7Utils.generateString();
        System.out.println("UUID v7 字符串: " + uuidStr); // 示例：018e9b1a-2d3e-7f8a-9b0c-1d2e3f4a5b6c

        // 解析生成时间
        Instant generateTime = UuidV7Utils.parseInstant(uuid);
        System.out.println("生成时间（UTC）: " + generateTime); // 示例：2024-09-06T10:15:30.123Z

        Instant utcInstant = UuidV7Utils.parseInstant(uuid);
        LocalDateTime chinaTime = LocalDateTime.ofInstant(utcInstant, ZoneId.of("Asia/Shanghai"));
        System.out.println("生成时间（中国区）: " + chinaTime);
    }

}

