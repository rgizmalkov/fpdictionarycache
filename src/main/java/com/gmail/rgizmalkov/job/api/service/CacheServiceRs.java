package com.gmail.rgizmalkov.job.api.service;

import java.util.List;
import java.util.Optional;

public interface CacheServiceRs<Rs> {

    int size();

    Optional<Rs> get(int num) throws IndexOutOfBoundsException;

    Optional<List<Rs>> response();
}
