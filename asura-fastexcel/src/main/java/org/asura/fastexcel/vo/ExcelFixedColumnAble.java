package org.asura.fastexcel.vo;

import java.util.Map;

public interface ExcelFixedColumnAble {

    Map<String, String> getHeadToIndexName();

    Map<String, Integer> getIndexNameToIndexValue();

    Map<String, String> getIndexNameToHead();

    Map<String, String> getFieldNameToIndexName();

}

