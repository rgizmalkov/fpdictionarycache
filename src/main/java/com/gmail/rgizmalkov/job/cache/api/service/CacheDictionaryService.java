package com.gmail.rgizmalkov.job.cache.api.service;

import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.Filter;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.FilterFactory;
import com.google.common.base.Optional;

import java.util.List;

public interface CacheDictionaryService {

    <Entity> Optional<Entity> get(FilterFactory filter, Class<Entity> tableClass);
    <Entity> Optional<Entity> get(Filter filter, Class<Entity> tableClass);

    <Entity> Optional<List<Entity>> few(FilterFactory filter, Class<Entity> tableClass);
    <Entity> Optional<List<Entity>> few(Filter filter, Class<Entity> tableClass);

    <Entity> Optional<List<Entity>> all(Class<Entity> tableClass);
}
