package org.akuma.aliyun;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    /**
     * 递归解析JSON对象，获取最终字段名和字段值
     *
     * @param object
     * @return
     */
    public static Map<String, Object> parse(Object object) {
        Map<String, Object> retMap = new HashMap<>();

        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
                String key = stringObjectEntry.getKey();
                Object value = stringObjectEntry.getValue();

                if (value instanceof JSONObject) {
                    Map<String, Object> map = parse(value);
                    retMap.putAll(map);
                }

                if (value instanceof JSONArray) {
                    if (value.toString().contains(":")) { // "PublicIpAddress": {"IpAddress": ["121.40.**.**"]}
                        Map<String, Object> map = parse(value);
                        retMap.putAll(map);
                    } else { // ["172.17.**.**"]
                        JSONArray jsonArray = (JSONArray) value;
                        List<String> list = new ArrayList<>();
                        for (Object o : jsonArray) {
                            list.add(o.toString());
                        }

                        retMap.put(key, String.join(",", list));
                    }
                }

                retMap.put(key, value);
            }
        } else if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            for (Object o : jsonArray) {
                Map<String, Object> map = parse(o);
                retMap.putAll(map);
            }
        } else {
            System.out.println("object is not JSONObject or JSONArray! object: " + object);
        }

        return retMap;
    }
}
