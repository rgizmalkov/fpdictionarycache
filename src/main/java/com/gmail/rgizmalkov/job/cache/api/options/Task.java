package com.gmail.rgizmalkov.job.cache.api.options;

import com.gmail.rgizmalkov.job.cache.api.service.CacheDictionaryService;
import com.google.common.base.Optional;


public interface Task<Rq,Rs> {

    Optional<Rs> apply(Rq request, CacheDictionaryService service);
}
