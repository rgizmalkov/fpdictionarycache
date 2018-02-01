package com.gmail.rgizmalkov.job.impl.v1.srv;

import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;
import com.gmail.rgizmalkov.job.impl.v1.api.options.Chain;
import com.gmail.rgizmalkov.job.impl.v1.api.row.BaseFilter;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter.Action;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter.Operation;
import com.gmail.rgizmalkov.job.impl.v1.api.row.LocalCachedTable;
import com.gmail.rgizmalkov.job.impl.v1.errors.SingleResultExpectedRuntimeException;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LocalCacheDictionaryService implements CacheDictionaryService {
    private final static Logger logger = LoggerFactory.getLogger(LocalCacheDictionaryService.class);
    private final ImmutableMap<String, LocalCachedTable> cache;

    public LocalCacheDictionaryService(ImmutableMap<String, LocalCachedTable> cache) {
        this.cache = cache;
    }

    private <Dictionary> List<Dictionary> enrich(Filter filter, Class<Dictionary> tableClass){
        Chain<Operation, Action> condition = filter.condition();
        LocalCachedTable localCachedTable = cache.get(tableClass.getSimpleName());
        if(localCachedTable == null){
            logger.warn("Table [" + tableClass.getSimpleName() + "] does not exist in cache!");
            return new ArrayList<>();
        }
        logger.info("Request to cache = SELECT * FROM "+tableClass.getSimpleName()+" WHERE " +filter.toString());
        List<Dictionary> target = new ArrayList<>();
        Operation element = condition.element();
        while(condition.hasNext()){
            narrow(element, localCachedTable);
        }
        for (List<BaseFilter.FilterContent> filterContents : filterScope) {
            target.addAll(narrow(filterContents, localCachedTable));
        }

        return target;
    }

    private LocalCachedTable arrow(Operation element, LocalCachedTable localCachedTable){
        switch (element.getCondition()){
            case EQ:
                localCachedTable = localCachedTable.findNewTableFrom(element.getKey(), element.getObject());
                break;
            case LIKE:
                localCachedTable = localCachedTable.likeNewTableFrom(element.getKey(), (String) element.getObject());
                break;
        }
        return localCachedTable;
    }

    private <Dictionary> List<Dictionary> narrow(Chain<Operation, Action> scope, LocalCachedTable localCachedTable){
        localCachedTable = arrow(scope.element(), localCachedTable);
        while(scope.hasNext()) {
            Action transition = scope.transition();
            Operation element = scope.element();
            localCachedTable = arrow(element, localCachedTable);
        }
        return localCachedTable.content();
    }

    private

    private <Dictionary> List<Dictionary> narrow(List<BaseFilter.FilterContent> scope, LocalCachedTable localCachedTable){
        for (BaseFilter.FilterContent filterContent : scope) {
            switch (filterContent.getAction()){
                case EQ:
                    localCachedTable = localCachedTable.findNewTableFrom(filterContent.getKey(), filterContent.getValue());
                    break;
                case LIKE:
                    localCachedTable = localCachedTable.likeNewTableFrom(filterContent.getKey(), (String) filterContent.getValue());
                    break;
            }
        }
        return localCachedTable.content();
    }

    @Override
    public <Dictionary> Optional<Dictionary> get(Filter filter, Class<Dictionary> tableClass) {
        List<Dictionary> enrich = enrich(filter, tableClass);
        if(enrich.size() != 1)
            throw new SingleResultExpectedRuntimeException("The only expected result. Current response size = " + enrich.size());
        return Optional.fromNullable(enrich.get(0));
    }

    @Override
    public <Dictionary> Optional<List<Dictionary>> few(Filter filter, Class<Dictionary> tableClass) {
        return null;
    }

    @Override
    public <Dictionary> Optional<List<Dictionary>> all(Class<Dictionary> tableClass) {
        return null;
    }
}
