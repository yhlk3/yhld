package com.example.console.processor.category;

import com.example.console.domain.CategoryDTO;
import com.example.console.domain.CategoryExportVO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryProcessor {
    public static List<CategoryExportVO> process(List<CategoryDTO> categories) {
        // 创建快速访问的缓存Map
        Map<Long, CategoryDTO> categoryMap = categories.stream()
                .collect(Collectors.toMap(CategoryDTO::getId, Function.identity()));

        // 存储计算结果
        Map<Long, LevelInfo> levelCache = new HashMap<>();

        return categories.stream().map(category -> {
            // 计算层级路径和缩进
            LevelInfo levelInfo = calculateLevelInfo(category, categoryMap, levelCache);

            return new CategoryExportVO(
                    category.getId(),
                    levelInfo.path,
                    generateIndentName(category.getName(), levelInfo.depth),
                    levelInfo.fullName
            );
        }).collect(Collectors.toList());
    }

    // 计算层级信息的内部方法
    private static LevelInfo calculateLevelInfo(CategoryDTO category,
                                                Map<Long, CategoryDTO> categoryMap,
                                                Map<Long, LevelInfo> cache) {
        if (cache.containsKey(category.getId())) {
            return cache.get(category.getId());
        }

        List<Long> pathIds = new ArrayList<>();
        int depth = 0;
        StringJoiner fullName = new StringJoiner(" > ");

        CategoryDTO current = category;
        while (current != null) {
            pathIds.add(0, current.getId());
            fullName.add(current.getName());
            depth++;

            Long parentId = current.getParentId();
            current = (parentId != null) ? categoryMap.get(parentId) : null;
        }

        String path = pathIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("."));

        LevelInfo info = new LevelInfo(path, depth - 1, fullName.toString());
        cache.put(category.getId(), info);
        return info;
    }

    // 生成带缩进的名称（每级缩进2个空格）
    private static String generateIndentName(String name, int depth) {
        return "  ".repeat(depth) + name;
    }

    // 层级信息缓存对象
    private static class LevelInfo {
        String path;
        int depth;
        String fullName;

        LevelInfo(String path, int depth, String fullName) {
            this.path = path;
            this.depth = depth;
            this.fullName = fullName;
        }
    }
}