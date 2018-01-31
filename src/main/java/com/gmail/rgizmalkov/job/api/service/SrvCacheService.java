package com.gmail.rgizmalkov.job.api.service;

import com.gmail.rgizmalkov.job.api.options.Function;
import com.gmail.rgizmalkov.job.api.row.Table;

import java.util.List;

public interface SrvCacheService<Rq,Rs, T extends Table> {

    SrvCacheService<Rq,Rs,T> tables(T ... tables);

    SrvCacheService<Rq,Rs,T> index(Class<?> table, String attributeId);

    SrvCacheService<Rq, Rs,T> task(Rq request, Function<Rq, Rs> function);

    SrvCacheService<Rq, Rs,T> tasks(List<Rq> requestList, Function<Rq, Rs> function);

    CacheServiceRs<Rs> response();
}
