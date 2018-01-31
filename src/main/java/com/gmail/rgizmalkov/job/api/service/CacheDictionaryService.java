package com.gmail.rgizmalkov.job.api.service;

import com.gmail.rgizmalkov.job.api.row.Filter;
import com.google.common.base.Optional;

import java.util.List;

public interface CacheDictionaryService {

    <Dictionary> Optional<Dictionary> get(Filter filter, Class<Dictionary> tableClass);

    <Dictionary> Optional<List<Dictionary>> few(Filter filter, Class<Dictionary> tableClass);

    <Dictionary> Optional<List<Dictionary>> all(Class<Dictionary> tableClass);
}
