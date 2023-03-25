package com.hua.project.project_first.utils;

import java.util.HashMap;

public class MapUtil {
    public static HashMap GetMap(String key , String value){
        HashMap hashMap = new HashMap();
        hashMap.put(key,value);
        return hashMap;
    }
}
