package com.formssafe.domain.result.service;

import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class ExcelExportService {

    public XSSFWorkbook createSheets() {
        return new XSSFWorkbook();
    }

    public void exportToExcel(XSSFWorkbook sheets, String sheetName, List<String> headers,
                              Object body) {
        XSSFSheet sheet = writeTableHeaderExcel(sheets, sheetName, headers);
        writeBody(sheets, sheet, body);
    }

    protected XSSFSheet writeTableHeaderExcel(XSSFWorkbook sheets, String sheetName, List<String> headers) {
        XSSFSheet sheet = sheets.createSheet(sheetName);
        XSSFFont font = sheets.createFont();
        CellStyle style = sheets.createCellStyle();

        writeHeader(sheet, font, style, headers);

        return sheet;
    }

    protected void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer intValue) {
            cell.setCellValue(intValue);
        } else if (value instanceof Double doubleValue) {
            cell.setCellValue(doubleValue);
        } else if (value instanceof Boolean booleanValue) {
            cell.setCellValue(booleanValue);
        } else if (value instanceof Long longValue) {
            cell.setCellValue(longValue);
        } else {
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(style);
    }

    protected void writeHeader(XSSFSheet sheet, XSSFFont font, CellStyle style, List<String> headers) {
        Row row = sheet.createRow(1);

        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        for (int i = 0; i < headers.size(); i++) {
            sheet.setColumnWidth(i, 7000);
            createCell(row, i, headers.get(i), style);
        }
    }

    protected abstract void writeBody(XSSFWorkbook sheets, XSSFSheet sheet, Object body);
}
