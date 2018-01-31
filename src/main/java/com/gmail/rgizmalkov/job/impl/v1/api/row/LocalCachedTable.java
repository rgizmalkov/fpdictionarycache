package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.api.row.CachedTable;
import com.gmail.rgizmalkov.job.api.row.Index;
import com.gmail.rgizmalkov.job.impl.v1.errors.NotDictionaryClassRuntimeException;
import com.gmail.rgizmalkov.job.mock.Dictionary;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class LocalCachedTable<Type> implements CachedTable<Type> {
    private final static Logger logger = LoggerFactory.getLogger(LocalCachedTable.class);
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

    private LocalCachedTable(
            Class<Type> type,
            ImmutableMap<String, Type> data,
            ImmutableMap<String, Class<?>> tablePredictableRows,
            Map<String, ImmutableIndex> mapping,
            Set<String> indexes

    ) {
        this.type = type;
        this.data = data;
        this.tablePredictableRows = tablePredictableRows;
        this.mapping.putAll(mapping);
        this.indexes.addAll(indexes);
    }

    public LocalCachedTable<Type> loadData(List<Type> objects) {
        if(data == null && objects != null){
            Map<String, Type> localDataMap = new HashMap<>();
            // index object uuid
            // Убрать нидекс и оставить просто табле?
            Table<String, Object, List<String>> indexes = HashBasedTable.create();
            for (Type object : objects) {
                String uuid = UUID.randomUUID().toString();
                localDataMap.put(uuid, object);
                for (String index : mapping.keySet()) {
                    Object value = getValueFromField(index, object);
                    boolean contains = indexes.contains(index, value);
                    if(value != null && !contains) {
                        indexes.put(index, value, Lists.newArrayList(uuid));
                    }else if(contains){
                        List<String> uuids = indexes.get(index, value);
                        uuids.add(uuid);
                    }
                }
            }
            this.data = ImmutableMap.copyOf(localDataMap);
            for (ImmutableIndex index : mapping.values()) {
                String indexCode = index.name();
                Map<Object, List<String>> row = indexes.row(indexCode);
                if(row != null){
                    index.enrich(row);
                }
            }
        }
        return this;
    }

    @Override
    public List<Type> content() {
        return data.values().asList();
    }

    public <O> LocalCachedTable newTableFrom(String key, O object){
        Map<String, Type> typeMap = p_find(key, object);
        return new LocalCachedTable<Type>(
                this.type, ImmutableMap.copyOf(typeMap), tablePredictableRows, mapping, indexes
        );
    }

    public <O> List<Type> find(String key, O object){
        return Lists.newArrayList(p_find(key, object).values());
    }


    public <O> Map<String, Type> p_find(String key, O object){
        if(key != null && tablePredictableRows.containsKey(key) && object.getClass().isAssignableFrom(tablePredictableRows.get(key))){
            ImmutableIndex immutableIndex = mapping.get(key);
            final Map<String, Type> types = new HashMap<>();
            if (immutableIndex != null) {
                return immutableIndex.find(object).or(Optional.of(new ArrayList<>())).transform(new Function<List<String>, Map<String, Type>>() {
                    public Map<String, Type> apply(List<String> input) {
                        for (String uuid : input) {
                            if(data.containsKey(uuid))
                                types.put(uuid, data.get(uuid));
                        }
                        return types;
                    }
                }).or(new HashMap<>());
            }else {
                for (String key_type : data.keySet()) {
                    Type type = data.get(key_type);
                    Object valueFromField = getValueFromField(key, this.type);
                    if(object.equals(valueFromField)){
                        types.put(key_type, type);
                    }
                }
            }
            return types;
        }else {
            logger.warn("Can not find column ["+key+"] in table " + type.getSimpleName());
            return new HashMap<>();
        }
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
            logger.info("Index [" + index + "] successfully added to table " + type.getSimpleName());
        }else {
            logger.warn("Index [" + index + "] did not add to table " + type.getSimpleName() + ". Caused by - 1. No available column in the table. 2. Type of index different from String.class.");
        }
        return this;
    }

    @Override
    public CachedTable<Type> dropIndex(String index) {
        Class<?> objectClass = tablePredictableRows.get(index);
        if(objectClass != null && String.class.isAssignableFrom(objectClass) && mapping.containsKey(index)){
            mapping.remove(index);
            logger.info("Index [" + index + "] successfully dropped from table " + type.getSimpleName());
        }else {
            logger.warn("Index [" + index + "] did not dropped from table " + type.getSimpleName() + ". Caused by - 1. No available column in the table. 2. Type of index different from String.class. 3. Index not present in the table.");
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
