package com.gmail.rgizmalkov.job.impl.v1.api.options;

import com.gmail.rgizmalkov.job.api.options.Function;
import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;
import com.google.common.base.Optional;

import java.util.concurrent.Callable;

public class DictionaryTask<Rq,Rs>{
    private final Rq request;
    private final Function<Rq, Rs> function;

    public DictionaryTask(Rq request, Function<Rq, Rs> function) {
        this.request = request;
        this.function = function;
    }

    public Optional<Rs> noConcurrentCall(CacheDictionaryService service){
        return function.apply(request, service);
    }
}
