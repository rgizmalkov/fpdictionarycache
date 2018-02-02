package com.gmail.rgizmalkov.job.cache.api.service;

import com.google.common.base.Optional;

import java.util.List;

public interface CacheServiceRs<Rs> {

    int size();

    Optional<Rs> get(int num) throws IndexOutOfBoundsException;

    List<Optional<Rs>> response();
}
