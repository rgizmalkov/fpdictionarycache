package com.gmail.rgizmalkov.job.api.row;

import com.google.common.collect.ImmutableMap;

public interface Index {

    String name();

    ImmutableMap<Object, String> comparisonMap();
}
