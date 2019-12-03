package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName TestPoi
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/24 14:53
 * @Version V1.0
 */
public class TestPoi {

    /**
     * XSSFWorkBook：工作簿
     XSSFSheet：工作表
     XSSFRow：行
     XSSFCell：单元格（列）
     */

    // 从Excel文件中读取数据
    @Test
    public void readExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("d:/hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);// 获取第一个Sheet对象
        // 简便的方法
        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
        // 关闭
        workbook.close();
    }

    // 从Excel文件中读取数据2
    @Test
    public void readExcel_2() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("d:/hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);// 获取第一个Sheet对象
        // 获取最后一行
        int rows = sheet.getLastRowNum();
        System.out.println("最后一行:"+rows);
        for(int i=0;i<=rows;i++){
            XSSFRow row = sheet.getRow(i);
            // 获取当前行的最后一个单元格
            short cells = row.getLastCellNum();
            for(short j=0;j<cells;j++){
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        // 关闭
        workbook.close();
    }

    // 2.2.2. 向Excel文件写入数据
    @Test
    public void writeExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("用户管理");
        // 创建行对象
        XSSFRow row0 = sheet.createRow(0);// 0表示第1行
        row0.createCell(0).setCellValue("ID");
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);// 1表示第2行
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("张三");
        row1.createCell(2).setCellValue("22");

        XSSFRow row2 = sheet.createRow(2);// 2表示第3行
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("李四");
        row2.createCell(2).setCellValue("20");

        // 使用输出流进行输出
        OutputStream out = new FileOutputStream(new File("D:/user.xlsx"));
        workbook.write(out);
        out.flush();
        out.close();
        // 关闭
        workbook.close();
    }
}
