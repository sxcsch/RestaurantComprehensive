/*
 * Copyright (c) 2019, Cardinal Operations and/or its affiliates. All rights reserved.
 * CARDINAL OPERATIONS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 集合处理，基础工具类
 * @author code-generator
 * @date 2020-06-11
 * @description
 */
@Slf4j
public class ContentBuilderUtil {
    private ContentBuilderUtil() {}

    public static Integer LIMIT = 3000;

    /**
     * 构建List dto转换为vo
     *
     * @param data  service 返回的dto集合
     * @param clazz 返出VO class
     * @return
     */
    public static <T> List<T> builderList(List<?> data, Class<T> clazz) {
        if (!CollectionUtils.isEmpty(data)) {
            if (clazz != null) {
                int size = data.size();
                List<T> voList = new ArrayList<>(size + (size / 2));
                data.forEach(item -> {
                    try {
                        T o = clazz.newInstance();
                        BeanUtils.copyProperties(item, o);
                        voList.add(o);
                    } catch (Exception e) {
                        log.error("执行对象Copy出现错误" + e.getMessage(), e);
                    }
                });
                return voList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 构建Object dto转换为vo
     *
     * @param data  service 返回的dto对象
     * @param clazz 返出VO class
     * @return
     */
    public static <T> T builderObject(Object data, Class<T> clazz) {
        if (data != null) {
            if (clazz != null) {
                try {
                    T o = clazz.newInstance();
                    BeanUtils.copyProperties(data, o);
                    return o;
                } catch (Exception e) {
                    log.error("执行对象Copy出现错误" + e.getMessage(), e);
                }
            }
        }
        return null;
    }

    /**
     * 截取 List<List<Object>>，返回新的集合，清空原集合
     *
     * @param dataList
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> dataList, int limit) {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        List<List<T>> lists = new ArrayList<>();
        int size = dataList.size();
        int num = size / limit;
        for (int i = 0; i < num; i++) {
            List<T> limitList = new ArrayList<>(limit);
            limitList.addAll(dataList.subList(0, limit));
            lists.add(limitList);
            dataList.subList(0, limit).clear();
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            lists.add(dataList);
        }
        return lists;
    }

    /**
     * 取交集
     *
     * @param elementLists
     * @param <T>
     * @return
     */
    public static <T> List<T> retainElementList(List<List<T>> elementLists) {
        Optional<List<T>> result = elementLists.parallelStream()
                        .filter(elementList -> elementList != null && (elementList).size() != 0).reduce((a, b) -> {
                            a.retainAll(b);
                            return a;
                        });
        return result.orElse(new ArrayList<>());
    }

    public static List<Integer> stringToList(String ids, String separator) {
        String[] split = ids.split(separator);
        return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
    }
}
