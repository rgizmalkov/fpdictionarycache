package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.api.row.CachedTable;
import com.gmail.rgizmalkov.job.api.row.Index;
import com.gmail.rgizmalkov.job.impl.v1.errors.NotDictionaryClassRuntimeException;
import com.gmail.rgizmalkov.job.mock.Dictionary;
import com.google.common.collect.*;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class LocalCachedTable<Type> implements CachedTable<Type> {
    private final Class<Type> type;
    private final Map<String, ImmutableIndex> mapping = new HashMap<>();
    private final Set<String> indexes = new HashSet<>();
    private ImmutableMap<String, Type> data;

    private final ImmutableMap<String, Class<?>> tablePredictableRows;

    public LocalCachedTable(Class<Type> type, Set<String> indexes) {
        this.type = type;
        checkTable(type);
        this.tablePredictableRows = ImmutableMap.copyOf(enrichedFields(type));
        this.indexes.addAll(indexes);
        init(indexes);
    }

    public LocalCachedTable<Type> loadData(List<Type> objects) {
        if(data == null && objects != null){
            Map<String, Type> localDataMap = new HashMap<>();
            // index object uuid
            // Убрать нидекс и оставить просто табле?
            Table<String, Object, String> indexes = HashBasedTable.create();
            for (Type object : objects) {
                String uuid = UUID.randomUUID().toString();
                localDataMap.put(uuid, object);
                for (String index : mapping.keySet()) {
                    Object value = getValueFromField(index, object);
                    if(value != null) {
                        indexes.put(index, value, uuid);
                    }
                }
            }
            this.data = ImmutableMap.copyOf(localDataMap);
            for (ImmutableIndex index : mapping.values()) {
                String indexCode = index.name();
                Map<Object, String> row = indexes.row(indexCode);
                if(row != null){
                    index.enrich(row);
                }
            }
        }
        return this;
    }

    private Object getValueFromField(String field, Object target) {
        try {
            Field objectField = type.getDeclaredField(field);
            ReflectionUtils.makeAccessible(objectField);
            return ReflectionUtils.getField(objectField, target);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Index get(String indexCode) {
        return mapping.get(indexCode);
    }

    @Override
    public boolean isPresent(String indexCode) {
        return mapping.containsKey(indexCode);
    }

    @Override
    public CachedTable<Type> addIndex(String index) {
        Class<?> objectClass = tablePredictableRows.get(index);
        if(objectClass != null && String.class.isAssignableFrom(objectClass)){
            mapping.put(index, new ImmutableIndex(index));
        }
        return this;
    }

    @Override
    public Class<Type> table() {
        return this.type;
    }

    @Override
    public ImmutableSet<String> indexes() {
        return ImmutableSet.copyOf(indexes);
    }

    private void checkTable(Class<Type> type) {
        Objects.requireNonNull(type, "[Cache service RE] Cache table class cannot be NULL!");
        if(!type.isAnnotationPresent(Dictionary.class)){
            throw new NotDictionaryClassRuntimeException("[Cache service RE] Cache table class have to be marked by @Dictionary!");
        }
    }


    private Map<String, Class<?>> enrichedFields(Class<Type> type) {
        Map<String, Class<?>> fields = new HashMap<>();
        for (Field declaredField : type.getDeclaredFields()) {
            fields.put(declaredField.getName(), declaredField.getType());
        }
        return fields;
    }

    private void init(Set<String> indexes) {
        for (String index : indexes) {
            this.addIndex(index);
        }
    }

}
