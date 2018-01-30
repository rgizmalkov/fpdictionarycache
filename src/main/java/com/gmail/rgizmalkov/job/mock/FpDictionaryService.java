package com.gmail.rgizmalkov.job.mock;

import java.util.List;
import java.util.Optional;

public interface FpDictionaryService {

    <Rs> Optional<Rs> get();
    <Rs> Optional<List<Rs>> few();
    <Rs> Optional<List<Rs>> all();
}
