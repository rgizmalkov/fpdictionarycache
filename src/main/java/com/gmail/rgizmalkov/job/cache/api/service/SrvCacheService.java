package com.gmail.rgizmalkov.job.cache.api.service;

import com.gmail.rgizmalkov.job.cache.api.options.Task;
import com.gmail.rgizmalkov.job.cache.api.options.TaskFactory;
import com.gmail.rgizmalkov.job.cache.api.row.Table;

import java.util.List;

public interface SrvCacheService<Rq,Rs, T extends Table> {

    SrvCacheService<Rq,Rs,T> tables(T ... tables);

    SrvCacheService<Rq,Rs,T> index(Class<?> table, String attributeId);

    SrvCacheService<Rq, Rs,T> task(Rq request, Task<Rq, Rs> task);

    SrvCacheService<Rq, Rs,T> tasks(List<Rq> requestList, Task<Rq, Rs> task);
    SrvCacheService<Rq, Rs,T> tasks(List<Rq> requestList, TaskFactory<Rq, Rs> task);

    CacheServiceRs<Rs> response();
}
