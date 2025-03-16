package com.example.module.excel.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class ExcelService {
    public <T> File exportSheet(String sheetName, Class<T> clazz, List<?> data) throws IOException {
        File tempFile = Files.createTempFile("export_", ".xlsx").toFile();
        // 复用原有样式设置
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        EasyExcel.write(tempFile, clazz)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new HorizontalCellStyleStrategy(headStyle, new WriteCellStyle()))
                .sheet(sheetName)
                .doWrite(data);

        return tempFile;
    }
}
