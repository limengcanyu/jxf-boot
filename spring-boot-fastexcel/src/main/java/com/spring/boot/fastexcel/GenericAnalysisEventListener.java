package com.spring.boot.fastexcel;

import cn.hutool.json.JSONUtil;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.metadata.CellExtra;

import java.util.LinkedHashMap;
import java.util.Map;

public class GenericAnalysisEventListener<F extends ExcelFixedColumnAble, D extends ExcelDynamicColumnAble> extends AnalysisEventListener<Map<String, Object>> {

    Map<String, Integer> HEAD_TO_INDEX_MAP = new LinkedHashMap<>();
    Map<String, String> HEAD_TO_FIELD_MAP = new LinkedHashMap<>();

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("Excel Head: " + JSONUtil.toJsonStr(headMap));
        headMap.forEach((k, v) -> {
            HEAD_TO_INDEX_MAP.put(v, k);
        });
        System.out.println("Excel HEAD_TO_INDEX_MAP: " + JSONUtil.toJsonStr(HEAD_TO_INDEX_MAP));
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        super.onException(exception, context);
    }

    @Override
    public void invoke(Map<String, Object> data, AnalysisContext context) {
        System.out.println("Excel Data: " + JSONUtil.toJsonStr(data));
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("Excel读取完成");
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return super.hasNext(context);
    }

}
