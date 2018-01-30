package com.gmail.rgizmalkov.job.api.row;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public interface CachedTable<Type> extends Table<Type> {

//    ImmutableMap<Index, Index> indexes();

    Index get(String indexCode);

    boolean isPresent(String indexCode);

    CachedTable<Type> addIndex(String indexCode);

}

