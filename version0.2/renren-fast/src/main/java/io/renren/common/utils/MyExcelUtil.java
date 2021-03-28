package io.renren.common.utils;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class MyExcelUtil {

    /**
     * 获取cell中的值并返回String类型
     *
     * @param cell
     * @return String类型的cell值
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (null != cell) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    if (0 == cell.getCellType()) {// 判断单元格的类型是否则NUMERIC类型
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 判断是否为日期类型
                            Date date = cell.getDateCellValue();
//                      DateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            DateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
                            cellValue = formater.format(date);
                        } else {
                            // 有些数字过大，直接输出使用的是科学计数法： 2.67458622E8 要进行处理
                            DecimalFormat df = new DecimalFormat("####.####");
                            cellValue = df.format(cell.getNumericCellValue());
                            // cellValue = cell.getNumericCellValue() + "";
                        }
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    cellValue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    cellValue = cell.getBooleanCellValue() + "";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    try {
                        // 如果公式结果为字符串
                        cellValue = String.valueOf(cell.getStringCellValue());
                    } catch (IllegalStateException e) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 判断是否为日期类型
                            Date date = cell.getDateCellValue();
//                      DateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            DateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
                            cellValue = formater.format(date);
                        } else {
                            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()
                                    .createFormulaEvaluator();
                            evaluator.evaluateFormulaCell(cell);
                            // 有些数字过大，直接输出使用的是科学计数法： 2.67458622E8 要进行处理
                            DecimalFormat df = new DecimalFormat("####.####");
                            cellValue = df.format(cell.getNumericCellValue());
//                          cellValue = cell.getNumericCellValue() + "";
                        }
                    }
//              //直接获取公式
//              cellValue = cell.getCellFormula() + "";
                    break;
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    cellValue = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        }
        return cellValue;
    }
    /**
     * 下载excel模板
     * @return
     * @throws Exception
     */
    public static Workbook exportExcel() throws Exception{
        System.out.println("文件模板生成");
        //可选择模板是.xls格式还是.xlsx格式
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个sheet表，可设置多个sheet
        Sheet sheet = workbook.createSheet("sheet1");
        sheet.setColumnWidth(0,6000 );
        sheet.setColumnWidth(1,6000 );
        sheet.setColumnWidth(2,6000 );
        // 3.创建单元格
        CellStyle cellStyle =workbook.createCellStyle();
        // 4.设置单元格的样式
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);


        String []tableHeader = new String[] {"1","2","3"};
        HSSFRow firstRow = (HSSFRow) sheet.createRow((short) 0);
        for(int i = 0; i < tableHeader.length; i++){
            HSSFCell cell = firstRow.createCell((short)i);
            cell.setCellStyle(workbook.createCellStyle());
            cell.setCellValue(tableHeader[i]);
        }
        return workbook;
    }


    public static void main(String[] args) {
        int sendSum = 0;
        String filePath = "D:\\ui\\uu.xls";
        InputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            Workbook workbook = null;
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls") || filePath.endsWith(".et")) {
                workbook = new HSSFWorkbook(fis);
            }
            fis.close();
            /* 读EXCEL文字内容 */
            // 获取第一个sheet表，也可使用sheet表名获取
            Sheet sheet = workbook.getSheetAt(0);
            // 获取行
            Iterator<Row> rows = sheet.rowIterator();
            Row row;
            Cell cell;

            while (rows.hasNext()) {
                row = rows.next();
                // 获取单元格
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = cells.next();
                    String cellValue = getCellValue(cell);
                    //读取到的每一次数据
                    System.out.print(cellValue + " ");
                    sendSum += 1;
                }
                //下一行
                System.out.println();
            }
            System.out.println("共发送数量:"+sendSum);
            //保存数据

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
