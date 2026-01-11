package com.spring.boot.fastexcel;

import cn.idev.excel.FastExcelFactory;

public class FastExcelFactoryTest {
    static void main() {
        FastExcelFactory.read("工作簿1.xlsx", new GenericAnalysisEventListener<>()).sheet(0).doRead();
    }
}
