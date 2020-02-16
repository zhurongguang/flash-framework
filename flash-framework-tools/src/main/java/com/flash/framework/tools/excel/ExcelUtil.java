package com.flash.framework.tools.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import java.io.File;
import java.util.List;

/**
 * 更多使用方式see https://github.com/alibaba/easyexcel
 *
 * @author zhurg
 * @date 2018/11/25 - 下午9:01
 */
public class ExcelUtil {

    /**
     * 读取简单excel
     *
     * @param file
     * @param sheetNo  sheetNo
     * @param model    Java数据类型
     * @param listener 监听器
     * @return
     */
    public static <T> AbstractExcelListener<T> readExcel(File file, int sheetNo, Class<?> model, AbstractExcelListener<T> listener) {
        EasyExcel.read(file, model, listener).sheet(sheetNo).doRead();
        return listener;
    }

    /**
     * 生成简单excel
     *
     * @param file
     * @param sheetNo   sheetNo
     * @param sheetName sheet名称
     * @param head      表头
     * @param datas     数据
     */
    public static void writeExcel(File file, int sheetNo, String sheetName, List<List<String>> head, List<?> datas) {
        EasyExcel.write(file)
                .sheet(sheetNo, sheetName)
                .head(head)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(datas);
    }

    /**
     * 生成简单excel
     *
     * @param file
     * @param sheetName
     * @param datas
     */
    public static void writeExcel(File file, String sheetName, List<?> datas) {
        EasyExcel.write(file, datas.get(0).getClass())
                .sheet(sheetName)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(datas);
    }

    /**
     * 基于模板生成简单excel
     *
     * @param template  excel模板
     * @param file
     * @param sheetNo   sheetNo
     * @param sheetName sheet名称
     * @param head      表头
     * @param datas     数据
     */
    public static void writeExcel(File template, File file, int sheetNo, String sheetName, List<List<String>> head, List<?> datas) {
        EasyExcel.write(file)
                .withTemplate(template)
                .sheet(sheetNo, sheetName)
                .head(head)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(datas);
    }
}