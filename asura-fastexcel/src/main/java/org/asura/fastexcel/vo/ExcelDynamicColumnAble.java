package org.asura.fastexcel.vo;

import java.util.Map;

public interface ExcelDynamicColumnAble {

    Map<String, String> getFixedColumnFieldNameToIndexName();

    Map<String, String> getDynamicColumnFieldName();

}

