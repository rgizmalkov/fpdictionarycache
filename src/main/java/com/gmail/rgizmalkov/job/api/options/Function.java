package com.gmail.rgizmalkov.job.api.options;

import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;

import java.util.Optional;

public interface Function<Rq,Rs> {

    Optional<Rs> apply(Rq request, CacheDictionaryService service);
}
