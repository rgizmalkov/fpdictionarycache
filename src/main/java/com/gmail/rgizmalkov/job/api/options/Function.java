package com.gmail.rgizmalkov.job.api.options;

import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;
import com.google.common.base.Optional;


public interface Function<Rq,Rs> {

    Optional<Rs> apply(Rq request, CacheDictionaryService service);
}
