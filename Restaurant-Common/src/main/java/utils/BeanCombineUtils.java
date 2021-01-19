package utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hannannan ( hannannan@shanshu.ai )
 * @date 2020/09/29
 **/
@Slf4j
public class BeanCombineUtils<T> {
    private static final String spliter = ",";

    /**
     * 将from 的字段 拷贝到target的字段 忽略空值 0值 和字段在ignores的值
     *
     * @param from
     * @param target
     * @param ignores
     */
    public static <T> void copyFields(T from, T target, String ignores) {
        List<String> ignoreList = Arrays.asList(ignores.split(spliter));
        Field[] allfields = getAllFields(from);
        for (Field field : allfields) {
            field.setAccessible(true);
            String name = field.getName();
            log.debug("copying field:" + name);
            if (ignoreList.contains(name)) {
                log.debug("ignore field:" + name);
                continue;
            }
            try {
                Object value = field.get(from);
                if (value == null || value.equals("null")) {
                    log.debug("nullable ,ignore field:" + name);
                    continue;
                }
                log.debug("set value to:" + name);
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error("set value error on field:{}", name);
            }

        }

    }


    /**
     * 比较两个对象
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean equal(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (obj1.equals(obj2)) {
            return true;
        }
        return false;
    }

    /**
     * 比较所有字段
     * 白名单优先
     *
     * @return
     */
    public static <T> Map<String, Boolean> comapreFields(T v1, T v2, List<String> whiteList, List<String> blackList) {
        Field[] allfields = getAllFields(v1);
        Map<String, Boolean> result = new HashedMap<>();

        for (Field field : allfields) {
            String fieldName = field.getName();
            field.setAccessible(true);
            boolean whiteListModel = whiteList != null && whiteList.size() > 0;
            if (whiteListModel && !whiteList.contains(fieldName)) {
                continue;
            }
            if (!whiteListModel && blackList != null && blackList.contains(fieldName)) {
                continue;
            }
            //开始比较
            boolean modified = false;
            try {
                modified = !Objects.equals(field.get(v1), field.get(v2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(fieldName, modified);
            log.debug("{} compare field:{} ,modified:{}", modified ? ">>>>" : "----", fieldName, modified);
        }
        return result;
    }

    public static <T> Map<String, Boolean> comapreFields(T v1, T v2, String whiteListString, String blackListString) {
        List<String> whiteList = StringUtils.isBlank(whiteListString) ? null : Arrays.asList(whiteListString.split(","));
        List<String> blackList = StringUtils.isBlank(blackListString) ? null : Arrays.asList(blackListString.split(","));
        return comapreFields(v1, v2, whiteList, blackList);
    }

    public static boolean calcBooleanFalse(Map<String, Boolean> map) {
        boolean re = false;
        for (String key : map.keySet()) {
            Boolean value = map.get(key);
            re = re || value;
        }
        return re;
    }

    public static boolean calcBooleanTrue(Map<String, Boolean> map) {
        boolean re = true;
        for (String key : map.keySet()) {
            Boolean value = map.get(key);
            re = re && value;
        }
        return re;
    }


    public static <T> void convertNullIntegerToZero(T t) {
        Field[] fields = getAllFields(t);
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> clazz = field.getType();
            try {
                if (field.get(t)!=null){
                    continue;
                }
                if (clazz.equals(Integer.class)) {
                    field.set(t, new Integer(0));
                } else if (clazz.equals(Long.class)) {
                    field.set(t, new Long(0L));
                } else if (clazz.equals(Double.class)) {
                    field.set(t, new Double(0.0));
                } else if (clazz.equals(Float.class)) {
                    field.set(t, new Float(0.0f));
                } else if (clazz.equals(BigDecimal.class)) {
                    field.set(t, new BigDecimal(0.0));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error("cannot set value to bean");
            }
        }

    }

    /**
     * 获取所有的字段
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> Field[] getAllFields(T t) {
        Field[] currentfields = t.getClass().getDeclaredFields();
        Field[] superfields = t.getClass().getSuperclass().getDeclaredFields();
        Field[] allfields = concat(currentfields, superfields);
        return allfields;
    }

    /**
     * 合并数据
     *
     * @param first
     * @param second
     * @param <T>
     * @return
     */
    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;

    }


}