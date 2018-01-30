package com.gmail.rgizmalkov.job.api.service;

import com.gmail.rgizmalkov.job.api.row.Filter;

import java.util.List;
import java.util.Optional;

public interface CacheDictionaryService {

    <TableInstance> Optional<TableInstance> get(Filter filter, Class<TableInstance> tableClass);

    <TableInstance> Optional<List<TableInstance>> few(Filter filter, Class<TableInstance> tableClass);

    <TableInstance> Optional<List<TableInstance>> all(Class<TableInstance> tableClass);
}
