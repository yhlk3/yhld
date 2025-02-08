package com.example.module.commo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&useAffectedRows=true",
                        "root", "123456")
                .globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(System.getProperty("user.dir") + "/module");// 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("/src/main/java") // 设置父包名
                                .moduleName("com.example.module") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "C:/springcode/store/module/src/main/resources/mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder -> {
                    builder.addInclude("category") // 设置需要生成的表名
                            .addTablePrefix() // 设置过滤表前缀
                            .mapperBuilder()
                            .mapperTemplate("/templates/mapper.java")
                            .entityBuilder()
                            .javaTemplate("/templates/entity.java") // 设置实体类模板
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .disableServiceImpl()
                            .serviceTemplate("/templates/service.java")// 设置 Service 模板
                            .controllerBuilder()
                            .disable()
                            .build();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
