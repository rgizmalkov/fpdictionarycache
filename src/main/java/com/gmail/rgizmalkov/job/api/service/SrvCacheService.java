package com.gmail.rgizmalkov.job.api.service;

import com.gmail.rgizmalkov.job.api.options.Function;
import com.gmail.rgizmalkov.job.api.row.Table;

import java.util.List;

public interface SrvCacheService<Rq,Rs> {

    SrvCacheService<Rq,Rs> tables(Table ... tables);

    SrvCacheService<Rq,Rs> index(Table table, String attributeId);

    SrvCacheService<Rq, Rs> task(Rq request, Function<Rq, Rs> function);

    SrvCacheService<Rq, Rs> tasks(List<Rq> requestList, Function<Rq, Rs> function);

    CacheServiceRs<Rs> response();
}
