package com.example.console.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.example.console.domain.CategoryDTO;
import com.example.console.domain.CategoryExportVO;
import com.example.console.domain.UserDTO;
import com.example.console.processor.category.CategoryProcessor;
import com.example.module.category.entity.Category;
import com.example.module.excel.service.ExcelService;
import com.example.module.user.entity.User;
import com.example.module.category.service.CategoryService;
import com.example.module.excel.entity.ExcelData;
import com.example.module.user.service.UserService;
import com.example.module.utils.Response;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class ExcelController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExcelService excelService;

//接⼝1：上传⼀个excel⽂件，读取内容，返回内容（列表）
    @RequestMapping("/upload/excel")
    public Response uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new Response(3012);
        }
        List<ExcelData> dataList = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream())
                    .head(ExcelData.class)
                    .sheet()
                    .registerReadListener(new AnalysisEventListener<ExcelData>() {
                        @Override
                        public void invoke(ExcelData data, AnalysisContext context) {
                            dataList.add(data); // 逐行读取
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            // 读取完成
                        }
                    }).doRead();
        } catch (IOException e) {
            return new Response(3011);
        }
        return new Response(1001, dataList);
    }
//接⼝2：⽣成excel⽂件，把分类表导出成excel⽂件，并直接下载
    @RequestMapping("/export/categories")
    public Response exportCategories(HttpServletResponse response) {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO DTO = new CategoryDTO();
            DTO.setId(category.getId());
            DTO.setName(category.getName());
            DTO.setParentId(category.getParentId());
            categoryDTOS.add(DTO);
        }
        List<CategoryExportVO> exportData = CategoryProcessor.process(categoryDTOS);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteCellStyle contentStyle = new WriteCellStyle();

        // 生成Excel
        try {
            EasyExcel.write(response.getOutputStream(), CategoryExportVO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headStyle, contentStyle))
                    .sheet("分类明细")
                    .doWrite(exportData);
        } catch (IOException e) {
            return new Response(3013);
        }
        return new Response(1001);
    }
    //接⼝3：多线程导出，多张表的数据导出成excel⽂件，并打压缩包，直接下载

    @RequestMapping("/export/multiple")
    public Response exportMultipleTables(HttpServletResponse response) {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setParentId(category.getParentId());
            categoryDTOS.add(categoryDTO);
        }
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setPhone(user.getPhone());
            userDTO.setNickName(user.getNickName());
            userDTO.setCreateTime(user.getCreateTime());
            userDTOS.add(userDTO);
        }
        List<CategoryExportVO> categoryData = CategoryProcessor.process(categoryDTOS);
        List<UserDTO> userData = userDTOS;
        ExecutorService executor = Executors.newFixedThreadPool(2);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=multi_export.zip");
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            List<Callable<File>> tasks = new ArrayList<>();
            tasks.add(() -> excelService.exportSheet("商品分类", CategoryExportVO.class, categoryData));
            tasks.add(() -> excelService.exportSheet("用户列表", UserDTO.class, userData));
            // 执行并获取结果
            List<Future<File>> futures = executor.invokeAll(tasks);
            // 打包压缩文件
            for (Future<File> future : futures) {
                File excelFile = future.get();
                ZipEntry zipEntry = new ZipEntry(excelFile.getName());
                zos.putNextEntry(zipEntry);
                Files.copy(excelFile.toPath(), zos);
                zos.closeEntry();
                excelFile.delete(); // 删除临时文件
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Response(3013);
        } finally {
            executor.shutdown();
        }
        return new Response(1001);
    }
}

