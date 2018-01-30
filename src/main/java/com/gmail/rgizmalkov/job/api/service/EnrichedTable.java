package com.gmail.rgizmalkov.job.api.service;

import com.gmail.rgizmalkov.job.api.row.CachedTable;
import com.gmail.rgizmalkov.job.api.row.Table;

/**
 * Работает по прнципу имя поля = имя параметра из фп справочников
 */
public interface EnrichedTable {

    <Type> CachedTable<Type> enriched(Table[] tables);
}
