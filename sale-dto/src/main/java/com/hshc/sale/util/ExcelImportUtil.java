package com.hshc.sale.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ExcelImportUtil {

    private final static String EXCEL_XLS = ".xls"; // 2003版以下的Excel

    private final static String EXCEL_XLSX = ".xlsx"; // 2007版以上的Excel

    /**
     * Excel导入
     * 
     * @param in 导入文件流
     * @param fileType 文件类型
     * @param keys 定义每列的值对应的key
     * @param paraMap 统计范围参数
     *            primary_key: 数据唯一校验标识
     *            primary_label: 标识描述
     *            start_row : 开始行
     *            start_col : 开始列
     * @return 定义key与cell值对应的Map列表
     * @throws Exception
     */
    public static List<Map<String, Object>> importFromExcel(InputStream in, String fileType, String[] keys, Map<String, Object> paraMap)
        throws Exception {
        // 返回数据列表
        List<Map<String, Object>> mapList = Lists.newArrayList();
        // 集合去重
        Set<Object> valset = Sets.newHashSet();
        if (paraMap == null) {
            paraMap = Maps.newHashMap();
        }
        String primary_key = paraMap.get("primary_key") == null ? "" : (String) paraMap.get("primary_key");
        String primary_label = paraMap.get("primary_label") == null ? "" : (String) paraMap.get("primary_label");
        int start_row = paraMap.get("start_row") == null ? 1 : (Integer) paraMap.get("start_row") - 1;
        int start_col = convert2Number(paraMap.get("start_col") == null ? 1 : paraMap.get("start_col")) - 1;

        // 取哪一个sheetName的页签 addby wp 2017.1.8
        String sheetName = paraMap.get("sheetName") == null ? "" : (String) paraMap.get("sheetName");
        try {
            // 创建Excel工作薄
            Workbook workbook = getWorkbook(in, fileType);
            // 遍历Excel中所有的sheet页
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // sheetName如果有值。则只去该页签的值。。addby wp 2017.1.8
                if (!StringUtils.isBlank(sheetName)) {
                    if (!sheetName.equals(sheet.getSheetName())) continue;
                }

                if (sheet == null) {
                    throw new Exception("导入文件第" + (i + 1) + "页Sheet页为空！");
                }
                // 遍历当前sheet页中的所有行
                for (int j = start_row; j < sheet.getPhysicalNumberOfRows(); j++) {
                    Row row = sheet.getRow(j);
                    if (row == null || row.getFirstCellNum() < 0) {
                        // 过滤掉空行
                        continue;
                    } else {
                        // 遍历所有的列
                        int keyIndex = 0;
                        Map<String, Object> cellMap = Maps.newHashMap();
                        for (int k = start_col; k < row.getLastCellNum(); k++) {
                            if (StringUtils.isBlank(keys[keyIndex])) {
                                throw new Exception("转换数据定义的Key中含有空值，请联系管理员处理！");
                            }
                            Cell cell = row.getCell(k);
                            // 跳过空行
                            if (cell == null) {
                                keyIndex++;
                                continue;
                            }
                            if (keyIndex >= keys.length) {
                                throw new Exception("无法读取第" + k + "至第" + row.getLastCellNum() + "列的数据，请删除多余的列！\nPS：如问题反复出现，请尝试新建Excel表格将内容粘贴进去重新导入！");
                            }
                            // 判断primary_key重复数据
                            if (StringUtils.equals(primary_key, keys[keyIndex])) {
                                if (valset.contains(getCellValue(cell))) {
                                    throw new Exception("导入文件中含有重复的" + primary_label + "：" + getCellValue(cell));
                                } else {
                                    valset.add(getCellValue(cell));
                                }
                            }
                            cellMap.put(keys[keyIndex], getCellValue(cell));
                            keyIndex++;
                        }
                        mapList.add(cellMap);
                    }
                }
            }
            return mapList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // 创建EXCEL工作薄对象
    private static Workbook getWorkbook(InputStream is, String fileType) throws Exception {
        Workbook workbook = null;
        if (EXCEL_XLS.equals(fileType)) {
            workbook = new HSSFWorkbook(is); // 2003-
        } else if (EXCEL_XLSX.equals(fileType)) {
            workbook = new XSSFWorkbook(is); // 2007+
        } else {
            throw new Exception("请导入Excel类型的文件！");
        }
        return workbook;
    }

    // 获取EXCEL表格内容值
    private static Object getCellValue(Cell cell) throws Exception {
        String value = null;
        DecimalFormat intFormat = new DecimalFormat("#"); // 格式化number String字符
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // 格式化数字
        try {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    // 是否日期类型
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = dateFormat.format(cell.getDateCellValue());
                    } else {
                        // 处理数字类型
                        String tempValue = cell.getNumericCellValue() + "";
                        if (tempValue.endsWith(".0")) {
                            value = intFormat.format(cell.getNumericCellValue());
                        } else {
                            value = decimalFormat.format(cell.getNumericCellValue());
                        }
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue() ? "1" : "0";
                    break;
                case Cell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                default:
                    break;
            }
            if (StringUtils.isBlank(value)) {
                throw new RuntimeException("第[" + (cell.getRowIndex() + 1) + "]行[" + (cell.getColumnIndex() + 1) + "]列数据为空！");
            }
            return value;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("第[" + (cell.getRowIndex() + 1) + "]行[" + (cell.getColumnIndex() + 1) + "]列导入数据类型/格式不正确！");
        }
    }

    private static int convert2Number(Object col) throws Exception {
        int colnum = 0;
        if (col instanceof String) {
            String colstr = ((String) col).toUpperCase();
            for (int i = 0; i < colstr.length(); i++) {
                char c = colstr.charAt(colstr.length() - i - 1);
                int num = (int) (c - 'A' + 1) * (int) Math.pow(26, i);
                colnum += num;
            }
        } else if (col instanceof Integer) {
            colnum = (Integer) col;
        } else {
            throw new Exception("开始列/结束列填写错误！");
        }
        return colnum;
    }
}
