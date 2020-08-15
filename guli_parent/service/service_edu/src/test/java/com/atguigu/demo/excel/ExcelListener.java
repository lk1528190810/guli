package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<DemoData> {

    //一行一行的读
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        System.out.println("******" + data);
    }

    //这是表头的信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头的信息" + headMap);
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
