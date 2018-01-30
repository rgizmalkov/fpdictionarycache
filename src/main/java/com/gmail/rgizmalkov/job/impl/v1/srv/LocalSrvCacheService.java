package com.gmail.rgizmalkov.job.impl.v1.srv;

import com.gmail.rgizmalkov.job.api.row.Table;
import com.gmail.rgizmalkov.job.api.options.Function;
import com.gmail.rgizmalkov.job.api.service.CacheServiceRs;
import com.gmail.rgizmalkov.job.api.service.SrvCacheService;
import com.gmail.rgizmalkov.job.mock.FpDictionaryService;

import java.util.List;

public class LocalSrvCacheService<Rq,Rs> implements SrvCacheService<Rq, Rs> {
    private final FpDictionaryService service;

    public LocalSrvCacheService(FpDictionaryService service) {
        this.service = service;
    }


    @Override
    public SrvCacheService<Rq, Rs> tables(Table... tables) {
        return null;
    }

    @Override
    public SrvCacheService<Rq, Rs> index(Table table, String attributeId) {
        return null;
    }

    @Override
    public SrvCacheService<Rq, Rs> task(Rq request, Function<Rq, Rs> function) {
        return null;
    }

    @Override
    public SrvCacheService<Rq, Rs> tasks(List<Rq> requestList, Function<Rq, Rs> function) {
        return null;
    }

    @Override
    public CacheServiceRs<Rs> response() {
        return null;
    }
}
