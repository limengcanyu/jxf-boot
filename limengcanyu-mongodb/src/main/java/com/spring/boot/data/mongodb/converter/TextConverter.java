package com.spring.boot.data.mongodb.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.metadata.property.StyleProperty;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.IndexedColors;

public class TextConverter implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        StyleProperty contentStyleProperty = contentProperty.getContentStyleProperty();
        contentStyleProperty.setWrapped(true); // 单元格内容通过\n换行

        // 设置单元格字体颜色
        WriteCellData<Object> objectWriteCellData = new WriteCellData<>(value);
        WriteCellStyle createStyle = objectWriteCellData.getOrCreateStyle();
        WriteFont writeFont = new WriteFont();
        switch (value) {
            case "1" -> writeFont.setColor(IndexedColors.BRIGHT_GREEN.getIndex());
            case "2" -> writeFont.setColor(IndexedColors.RED.getIndex());
            case "3" -> writeFont.setColor(IndexedColors.LIGHT_ORANGE.getIndex());
            default -> writeFont.setColor(IndexedColors.BLUE.getIndex());
        }
        createStyle.setWriteFont(writeFont);

        return objectWriteCellData;
    }
}
