package com.gmail.rgizmalkov.job.cache.impl.v1.api.options;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class MassiveFilter {
    private MassiveFilter(){}

    public static <K,V> Map<K,V> like(Map<K,V> source, String pattern){
        Pattern compile = Pattern.compile(pattern);
        Map<K, V> target = new HashMap<>();
        for (K key : source.keySet()) {
            if(compile.matcher(key.toString()).find()){
                target.put(key, source.get(key));
            }
        }
        return target;
    }
     
    public static <K,V> Map<K,V> likeValueField(Map<K,V> source, String pattern, String field){
        Pattern compile = Pattern.compile(pattern);
        Map<K, V> target = new HashMap<>();
        for (K key : source.keySet()) {
            V value = source.get(key);
            Object object = getFieldOfObject(field, value);
            if(object != null && compile.matcher(object.toString()).find()){
                target.put(key, value);
            }
        }
        return target;
    }

    public static <K,V,O> Map<K,V> eqValueField(Map<K,V> source, O comparable, String field){
        Map<K, V> target = new HashMap<>();
        for (K key : source.keySet()) {
            V value = source.get(key);
            Object object = getFieldOfObject(field, value);
            if(comparable.equals(object)){
                target.put(key, value);
            }
        }
        return target;
    }

    public static Object getFieldOfObject(String field, Object object) {
        if(object!= null && field!=null){
            Class<?> objectClass = object.getClass();
            Field targetField = ReflectionUtils.findField(objectClass, field);
            if (targetField != null) {
                ReflectionUtils.makeAccessible(targetField);
                return ReflectionUtils.getField(targetField, object);
            }
        }
        return null;
    }
}
