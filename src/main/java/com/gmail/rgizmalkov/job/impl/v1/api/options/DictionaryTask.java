package com.gmail.rgizmalkov.job.impl.v1.api.options;

import com.gmail.rgizmalkov.job.api.options.Function;
import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;
import com.google.common.base.Optional;

import java.util.concurrent.Callable;

public class DictionaryTask<Rq,Rs> implements Callable<Optional<Rs>> {
    private final Rq request;
    private final Function<Rq, Rs> function;
    private final CacheDictionaryService service;

    public DictionaryTask(Rq request, Function<Rq, Rs> function, CacheDictionaryService service) {
        this.request = request;
        this.function = function;
        this.service = service;
    }

    @Override
    public Optional<Rs> call() throws Exception {
        return function.apply(request, service);
    }

    public Optional<Rs> noConcurrentCall(){
        return function.apply(request, service);
    }
}
