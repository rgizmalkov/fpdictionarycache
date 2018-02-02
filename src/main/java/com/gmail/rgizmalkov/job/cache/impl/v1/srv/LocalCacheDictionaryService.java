package com.gmail.rgizmalkov.job.cache.impl.v1.srv;

import com.gmail.rgizmalkov.job.cache.api.service.CacheDictionaryService;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.options.Chain;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.options.Chain.Function;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.Filter;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.Filter.Action;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.Filter.Operation;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.FilterFactory;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.LocalCachedTable;
import com.gmail.rgizmalkov.job.cache.impl.v1.errors.AnyResultExpectedRuntimeException;
import com.gmail.rgizmalkov.job.cache.impl.v1.errors.SingleResultExpectedRuntimeException;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LocalCacheDictionaryService implements CacheDictionaryService {
    private final static Logger logger = LoggerFactory.getLogger(LocalCacheDictionaryService.class);
    private final ImmutableMap<String, LocalCachedTable> cache;

    public LocalCacheDictionaryService(ImmutableMap<String, LocalCachedTable> cache) {
        this.cache = cache;
    }



    private  <Entity> Map<String, Entity> single(Filter filter,  Class<Entity> tableClass){
        final LocalCachedTable localCachedTable = cache.get(tableClass.getSimpleName());
        if(localCachedTable == null){
            logger.warn("Table [" + tableClass.getSimpleName() + "] does not exist in cache!");
            return new HashMap<>();
        }
        logger.info("Cache request = SELECT * FROM "+tableClass.getSimpleName()+" WHERE " + filter.toString());
        return filter.condition().compress(new TableAggregation<Entity>(localCachedTable), new MergeFunction<>());
    }


    private <Entity> Map<String, Entity>  multiple(FilterFactory filter, Class<Entity> tableClass) {
        final LocalCachedTable localCachedTable = cache.get(tableClass.getSimpleName());
        if(localCachedTable == null){
            logger.warn("Table [" + tableClass.getSimpleName() + "] does not exist in cache!");
            return new HashMap<>();
        }
        logger.info("Cache request = SELECT * FROM "+tableClass.getSimpleName()+" WHERE " + filter.toString());
        return filter.chain().compress(new FilterAggregation<Entity>(tableClass), new MergeFunction<>());
    }

    @Override
    public <Entity> Optional<Entity> get(FilterFactory filter, Class<Entity> tableClass) {
        List<Entity> enrich = Lists.newArrayList(multiple(filter, tableClass).values());
        if(enrich.size() != 1)
            throw new SingleResultExpectedRuntimeException("The only expected result. Current response size = " + enrich.size());
        return Optional.fromNullable(enrich.get(0));
    }


    @Override
    public <Entity> Optional<List<Entity>> few(FilterFactory filter, Class<Entity> tableClass) {
        List<Entity> enrich = Lists.newArrayList(multiple(filter, tableClass).values());
        if(enrich.size() < 1)
            throw new AnyResultExpectedRuntimeException("Not the only expected result. Current response size = " + enrich.size());
        return Optional.of(enrich);
    }

    @Override
    public <Entity> Optional<Entity> get(Filter filter, Class<Entity> tableClass) {
        List<Entity> enrich = Lists.newArrayList(single(filter, tableClass).values());
        if(enrich.size() != 1)
            throw new SingleResultExpectedRuntimeException("The only expected result. Current response size = " + enrich.size());
        return Optional.fromNullable(enrich.get(0));
    }


    @Override
    public <Entity> Optional<List<Entity>> few(Filter filter, Class<Entity> tableClass) {
        List<Entity> enrich = Lists.newArrayList(single(filter, tableClass).values());
        if(enrich.size() < 1)
            throw new AnyResultExpectedRuntimeException("Not the only expected result. Current response size = " + enrich.size());
        return Optional.of(enrich);
    }

    @Override
    public <Entity> Optional<List<Entity>> all(Class<Entity> tableClass) {
        return Optional.fromNullable(getAll(tableClass));
    }

    private<Entity> List<Entity> getAll(Class<Entity> tableClass){
        final LocalCachedTable localCachedTable = cache.get(tableClass.getSimpleName());
        if(localCachedTable == null){
            logger.warn("Table [" + tableClass.getSimpleName() + "] does not exist in cache!");
            return new ArrayList<>();
        }
        logger.info("Cache request = SELECT * FROM "+tableClass.getSimpleName());
        return localCachedTable.content();
    }

    private class TableAggregation<Entity> implements Chain.Aggregation<Map<String,Entity>,Operation>{
        private LocalCachedTable<Entity> localCachedTable;

        private TableAggregation(LocalCachedTable<Entity> localCachedTable) {
            this.localCachedTable = localCachedTable;
        }

        public Map<String, Entity> transform(Operation source) {
            switch (source.getCondition()){
                case EQ:
                    return localCachedTable.find(source.getKey(), source.getObject());
                case LIKE:
                    return localCachedTable.like(source.getKey(), (String) source.getObject());
            }
            return new HashMap<>();
        }
    }

    private class FilterAggregation<Entity> implements Chain.Aggregation<Map<String,Entity>, Filter>{
        private Class<Entity> table;

        public FilterAggregation(Class<Entity> table) {
            this.table = table;
        }

        @Override
        public Map<String, Entity> transform(Filter filter) {
            return single(filter, table);
        }
    }

    private class MergeFunction<Entity> implements  Function<Map<String, Entity>, Action, Map<String, Entity>>{
        @Override
        public Map<String, Entity> apply(Map<String, Entity> left, Action action, Map<String, Entity> right) {
            switch (action) {
                case AND:
                    return  Maps.difference(left, right).entriesInCommon();
                case OR:
                    HashMap<String, Entity> localMap = Maps.newHashMap(left);
                    localMap.putAll(right);
                    return localMap;
            }
            return new HashMap<>();
        }
    }
}
