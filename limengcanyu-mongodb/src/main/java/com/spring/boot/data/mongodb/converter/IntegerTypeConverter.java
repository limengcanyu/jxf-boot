package com.spring.boot.data.mongodb.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class IntegerTypeConverter implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return switch (value) {
            case 1 -> new WriteCellData<>("value1");
            case 2 -> new WriteCellData<>("value2");
            default -> new WriteCellData<>("");
        };
    }

}
