package com.MTDap.commons.util;

import java.util.Map;

public class StringUtils {
    public static String convertMapToString(Map<String, ?> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            Object obj = map.get(key);
            if (obj.getClass().equals("string".getClass())) {
                mapAsString.append("\"" + key + "\"" + ":" + "\"" + obj + "\"" + ", ");
            } else {
                mapAsString.append("\"" + key + "\"" + ":" + obj + ", ");
            }
        }
        mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
}
