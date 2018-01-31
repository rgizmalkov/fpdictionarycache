package com.gmail.rgizmalkov.job.api.row;

import com.google.common.collect.ImmutableMap;

import java.util.List;

public interface Index {

    String name();

    ImmutableMap<Object, List<String>> comparisonMap();
}
