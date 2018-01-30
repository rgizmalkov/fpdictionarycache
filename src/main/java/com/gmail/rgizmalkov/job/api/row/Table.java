package com.gmail.rgizmalkov.job.api.row;

import com.google.common.collect.ImmutableSet;

public interface Table<Type> {

    Class<Type> table();

    ImmutableSet<String> indexes();
}
