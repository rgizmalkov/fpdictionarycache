package com.gmail.rgizmalkov.job.cache.impl.v1.api.options;

import com.gmail.rgizmalkov.job.cache.api.options.Task;
import com.gmail.rgizmalkov.job.cache.api.service.CacheDictionaryService;
import com.google.common.base.Optional;

public class DictionaryTask<Rq,Rs>{
    private final Rq request;
    private final Task<Rq, Rs> task;

    public DictionaryTask(Rq request, Task<Rq, Rs> task) {
        this.request = request;
        this.task = task;
    }

    public Optional<Rs> noConcurrentCall(CacheDictionaryService service){
        return task.apply(request, service);
    }
}
