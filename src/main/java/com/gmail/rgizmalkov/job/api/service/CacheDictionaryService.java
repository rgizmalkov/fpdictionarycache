package com.gmail.rgizmalkov.job.api.service;

import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter;
import com.gmail.rgizmalkov.job.impl.v1.api.row.FilterFactory;
import com.google.common.base.Optional;

import java.util.List;

public interface CacheDictionaryService {

    <Dictionary> Optional<Dictionary> get(FilterFactory filter, Class<Dictionary> tableClass);
    <Dictionary> Optional<Dictionary> get(Filter filter, Class<Dictionary> tableClass);

    <Dictionary> Optional<List<Dictionary>> few(FilterFactory filter, Class<Dictionary> tableClass);
    <Dictionary> Optional<List<Dictionary>> few(Filter filter, Class<Dictionary> tableClass);

    <Dictionary> Optional<List<Dictionary>> all(Class<Dictionary> tableClass);
}
