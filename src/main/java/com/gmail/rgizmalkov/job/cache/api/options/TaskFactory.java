package com.gmail.rgizmalkov.job.cache.api.options;


public interface TaskFactory<Rq,Rs> {
    Task<Rq,Rs> get();
}
