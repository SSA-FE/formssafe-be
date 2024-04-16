package com.formssafe.domain.result.service;

import com.formssafe.domain.result.dto.ResultExportResponse.ResultExportRow;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResultExcelExportService extends ExcelExportService {

    @Override
    public void writeBody(XSSFWorkbook sheets, XSSFSheet sheet, Object body) {
        List<ResultExportRow> bodyValue = (List<ResultExportRow>) body;

        CellStyle style = getFontContentExcel(sheets);

        int startRow = 2;
        for (ResultExportRow resp : bodyValue) {
            Row row = sheet.createRow(startRow++);
            int columnCount = 0;

            createCell(row, columnCount++, resp.responseAt(), style);
            createCell(row, columnCount++, resp.nickname(), style);
            for (String response : resp.responses()) {
                createCell(row, columnCount++, response, style);
            }
        }
    }

    private CellStyle getFontContentExcel(XSSFWorkbook sheets) {
        CellStyle style = sheets.createCellStyle();
        XSSFFont font = sheets.createFont();

        font.setFontHeight(14);
        style.setFont(font);

        return style;
    }
}
