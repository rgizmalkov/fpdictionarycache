package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.api.row.Index;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Индекс на столбец.
 * Object - значение объекта индексируемого столбца
 * String - уникальный идетификатор объекта в таблице
 */
public class ImmutableIndex implements Index {
    private final String index;
    private ImmutableMap<Object, List<String>> comparisonMap;

    public ImmutableIndex(String index) {
        this.index = index;
    }

    public Index enrich(Map<Object, List<String>> objects) {
        if (comparisonMap == null && objects != null) {
            this.comparisonMap = ImmutableMap.copyOf(objects);
        }
        return this;
    }

    public Optional<List<String>> find(Object key){
        return Optional.fromNullable(comparisonMap.get(key));
    }

    @Override
    public String name() {
        return index;
    }

    @Override
    public ImmutableMap<Object, List<String>> comparisonMap() {
        return this.comparisonMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImmutableIndex)) return false;
        ImmutableIndex that = (ImmutableIndex) o;
        return Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
