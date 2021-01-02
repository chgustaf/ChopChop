package com.examples.caseupdater.client.mapper;

import com.examples.caseupdater.client.domain.Account;
import java.util.HashMap;
import java.util.Map;

public class SObjectUtil {

  static Map<String, Class<?>> classMap;
  public SObjectUtil() {
    classMap = new HashMap<>();
    classMap.put("account", Account.class);
  }

  public static Class getClazz(String name) {
    return classMap.get(name);
  }
}
