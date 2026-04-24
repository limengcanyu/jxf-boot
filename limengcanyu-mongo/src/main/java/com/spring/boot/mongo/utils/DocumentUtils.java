package com.spring.boot.mongo.utils;

import org.bson.Document;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * date 2019/09/04
 */
public class DocumentUtils {
    public static String getFieldString(Document record, String fieldName) {
        return ObjectUtils.isEmpty(record) ? "" : (ObjectUtils.isEmpty(record.get(fieldName)) ? "" : record.get(fieldName).toString());
    }

    public static int getFieldInt(Document record, String fieldName) {
        return ObjectUtils.isEmpty(record) ? 0 : (ObjectUtils.isEmpty(record.get(fieldName)) ? 0 : Integer.parseInt(record.get(fieldName).toString()));
    }

    public static double getFieldDouble(Document record, String fieldName) {
        return ObjectUtils.isEmpty(record) ? 0.00 : (ObjectUtils.isEmpty(record.get(fieldName)) ? 0.00 : Double.parseDouble(record.get(fieldName).toString()));
    }

    public static BigDecimal getFieldBigDecimal(Document record, String fieldName) {
        return ObjectUtils.isEmpty(record) ? BigDecimal.ZERO : (ObjectUtils.isEmpty(record.get(fieldName)) ? BigDecimal.ZERO : new BigDecimal(record.get(fieldName).toString()));
    }
}
