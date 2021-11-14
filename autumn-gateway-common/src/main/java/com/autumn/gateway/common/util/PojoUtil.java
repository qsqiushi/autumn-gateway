package com.autumn.gateway.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-09-15 10:07
 */
public class PojoUtil {

  public static boolean compareContract(Object source, Object target, List<String> ignoreField) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      Field[] fields = getAllFields(source);
      for (int j = 0; j < fields.length; j++) {
        fields[j].setAccessible(true);

        if (ignoreField.contains(fields[j].getName())) {
          continue;
        }
        // 字段值
        if (!fields[j].get(source).equals(fields[j].get(target))) {
          return Boolean.FALSE;
        }
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return Boolean.TRUE;
  }

  /**
   * 获取所有属性，包括父类
   *
   * @param object
   * @return
   */
  private static Field[] getAllFields(Object object) {
    Class clazz = object.getClass();
    List<Field> fieldList = new ArrayList<>();
    while (clazz != null) {
      fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
      clazz = clazz.getSuperclass();
    }
    Field[] fields = new Field[fieldList.size()];
    fieldList.toArray(fields);
    return fields;
  }
}
