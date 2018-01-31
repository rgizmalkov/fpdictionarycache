package com.gmail.rgizmalkov.job.mock;

import com.google.common.base.Optional;

import java.util.List;

public interface FpDictionaryService {

    <Rs> Optional<Rs> get();
    <Rs> Optional<List<Rs>> few();
    <Rs> Optional<List<Rs>> all(Class<?> table);
}
