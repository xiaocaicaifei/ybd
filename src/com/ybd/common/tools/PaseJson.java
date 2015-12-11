package com.ybd.common.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ybd.common.L;

/**
 * 将json对象解析
 * 
 * @author caiyanfei
 * @version $Id: PaseJson.java, v 0.1 2015-2-13 下午4:35:31 caiyanfei Exp $
 */
public class PaseJson {

    /**
     * 将json字符串解析成对象（包括Array和Object通用的类型）
     */
    public static Object paseJsonToObject(String json) {
        try {
            if (json.substring(0, 1).equals("[")) {
                try {
                    JSONArray array = new JSONArray(json);
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Iterator<String> keyIter = jsonObject.keys();
                        Map<String, Object> map = new HashMap<String, Object>();
                        while (keyIter.hasNext()) {
                            String keyStr = keyIter.next();
                            String keyValue = jsonObject.get(keyStr).toString();
                            if (keyValue.length() > 0
                                && (keyValue.substring(0, 1).equals("[") || keyValue.substring(0, 1)
                                    .equals("{"))) {
                                map.put(keyStr, paseJsonToObject(keyValue));
                            } else {
                                map.put(keyStr, keyValue.equals("null") ? "" : keyValue);
                            }
                        }
                        list.add(map);
                    }
                    return list;
                } catch (Exception e) {//如果出现异常，说明是数组形式的
                    JSONArray array = new JSONArray(json);
                    String str[]=new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        str[i]=array.get(i).toString();
                    }
                    return str;
                }
                
            } else if (json.substring(0, 1).equals("{")) {
                try {
                    JSONObject object = new JSONObject(json);
                    Iterator<String> keyIter = object.keys();
                    Map<String, Object> map = new HashMap<String, Object>();
                    while (keyIter.hasNext()) {
                        String keyStr = keyIter.next();
                        String keyValue = object.get(keyStr).toString();
                        if (keyValue.length() > 0
                            && (keyValue.substring(0, 1).equals("[") || keyValue.substring(0, 1)
                                .equals("{"))) {
                            map.put(keyStr, paseJsonToObject(keyValue));
                        } else {
                            map.put(keyStr, keyValue.equals("null") ? "" : keyValue);
                        }
                    }
                    return map;
                } catch (Exception e) {
                    JSONArray array = new JSONArray(json);
                    String str[]=new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        str[i]=array.get(i).toString();
                    }
                    return str;
                }
                
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取Map中的值
     * 
     * @return
     */
    public static String getMapMsg(Map<String, Object> map,String name){
        try {
            return map.get(name).toString();
        } catch (Exception e) {
            L.v("获取Map信息时"+name+"参数为空");
            return "";
        }
    }
}
