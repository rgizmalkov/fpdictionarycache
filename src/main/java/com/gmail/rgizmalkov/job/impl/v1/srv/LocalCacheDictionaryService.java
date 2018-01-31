package com.gmail.rgizmalkov.job.impl.v1.srv;

import com.gmail.rgizmalkov.job.api.row.Filter;
import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;
import com.gmail.rgizmalkov.job.impl.v1.api.row.BaseFilter;
import com.gmail.rgizmalkov.job.impl.v1.api.row.LocalCachedTable;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class LocalCacheDictionaryService implements CacheDictionaryService {
    private final ImmutableMap<String, LocalCachedTable> cache;

    public LocalCacheDictionaryService(ImmutableMap<String, LocalCachedTable> cache) {
        this.cache = cache;
    }

    private void enrichStatement(Filter filter) {
        List<List<BaseFilter.FilterContent>> list = filter.filter();
        for (List<BaseFilter.FilterContent> filterContents : list) {

        }
    }

    private <Dictionary> List<Dictionary> enrich(Filter filter, Class<Dictionary> tableClass){
        List<List<BaseFilter.FilterContent>> list = filter.filter();
        boolean simpleCase = list.size() == 1;
        LocalCachedTable localCachedTable = cache.get(tableClass.getSimpleName());
        // TODO error if not find table
        boolean sign = true;
        if(simpleCase){
            List<BaseFilter.FilterContent> filterContents = list.get(0);
            for (BaseFilter.FilterContent filterContent : filterContents) {
                switch (filterContent.getAction()){
                    case EQ:
                        localCachedTable = localCachedTable.newTableFrom(filterContent.getKey(), filterContent.getValue());
                }
            }
        }
        return localCachedTable.content();

    }


    @Override
    public <Dictionary> Optional<Dictionary> get(Filter filter, Class<Dictionary> tableClass) {
        List<Dictionary> enrich = enrich(filter, tableClass);
        if(enrich.size() != 1)
            throw new RuntimeException("Error");
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
