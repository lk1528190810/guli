package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTest {
    public static void main(String[] args) {
        //1.定义文件的路径信息
        String fileName = "I:\\write.xlsx";

        //2. 这是写的方法
//        EasyExcel.write(fileName,DemoData.class).sheet("学生信息").doWrite(getData());

        //3.这是读的方法
        EasyExcel.read(fileName,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("lucy" + i);
            list.add(demoData);
        }
        return list;
    }
}
