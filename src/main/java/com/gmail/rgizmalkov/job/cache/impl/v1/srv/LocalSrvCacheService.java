package com.gmail.rgizmalkov.job.cache.impl.v1.srv;

import com.gmail.rgizmalkov.job.cache.api.options.Task;
import com.gmail.rgizmalkov.job.cache.api.options.TaskFactory;
import com.gmail.rgizmalkov.job.cache.api.service.CacheDictionaryService;
import com.gmail.rgizmalkov.job.cache.api.service.CacheServiceRs;
import com.gmail.rgizmalkov.job.cache.api.service.SrvCacheService;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.options.DictionaryTask;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.LocalCachedTable;
import com.gmail.rgizmalkov.job.mock.FpDictionaryService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

public class LocalSrvCacheService<Rq, Rs> implements SrvCacheService<Rq, Rs, LocalCachedTable> {
    private final static Logger logger = LoggerFactory.getLogger(LocalSrvCacheService.class);

    private final FpDictionaryService service;
    private CacheDictionaryService cacheDictionaryService;
    private ImmutableMap<String, LocalCachedTable> tables;
    private final List<DictionaryTask<Rq, Rs>> tasks = new ArrayList<>();

    public LocalSrvCacheService(FpDictionaryService service) {
        this.service = service;
    }

    @Override
    public SrvCacheService<Rq, Rs, LocalCachedTable> tables(LocalCachedTable... tables) {
        logger.info("Initial size of local cache service is " + (tables != null ? tables.length : 0));
        if (tables != null && tables.length > 0) {
            HashMap<String, LocalCachedTable> local_tables = new HashMap<>();
            for (LocalCachedTable table : tables) {
                local_tables.put(table.table().getSimpleName(), table);
            }
            this.tables = ImmutableMap.copyOf(local_tables);
        }
        return this;
    }

    @Override
    public SrvCacheService<Rq, Rs, LocalCachedTable> index(Class<?> dictionary, String attributeId) {
        String operationUUID = UUID.randomUUID().toString();
        logger.info(format("Add index [%s] to dictionary %s. [%s]", attributeId, dictionary.getSimpleName(), operationUUID));
        if(tables != null){
            LocalCachedTable table = tables.get(dictionary.getSimpleName());
            if(table != null){
                table.addIndex(attributeId);
            }else {
                logger.warn("Operation [" + operationUUID + "] ignored. Caused by - no available table in the service.");
            }
        }else {
            logger.warn("Operation [" + operationUUID + "] ignored. Caused by - tables have to be initialized before indexing.");
        }
        return this;
    }

    @Override
    public SrvCacheService<Rq, Rs, LocalCachedTable> task(Rq request, Task<Rq, Rs> task) {
        if (request != null && task != null) {
            this.tasks.add(new DictionaryTask<>(request, task));
        }else {
            logger.warn("Add task operation was ignored. Caused by - request or function is null.");
        }
        return this;
    }

    @Override
    public SrvCacheService<Rq, Rs, LocalCachedTable> tasks(List<Rq> requestList, Task<Rq, Rs> task) {
        if (requestList != null && task != null) {
            for (Rq rq : requestList) {
                this.task(rq, task);
            }
        }else {
            logger.warn("Add tasks operation was ignored. Caused by - request list or function is null.");
        }
        return this;
    }

    @Override
    public SrvCacheService<Rq, Rs, LocalCachedTable> tasks(List<Rq> requestList, TaskFactory<Rq, Rs> factory) {
        if (requestList != null && factory != null) {
            for (Rq rq : requestList) {
                this.task(rq, factory.get());
            }
        }else {
            logger.warn("Add tasks operation was ignored. Caused by - request list or function is null.");
        }
        return this;
    }

    @Override
    public CacheServiceRs<Rs> response() {
        loadData();
        List<Optional<Rs>> responses = runTasks();
        return new CacheServiceRs<Rs>() {
            @Override
            public int size() {
                return responses.size();
            }

            @Override
            public Optional<Rs> get(int num) throws IndexOutOfBoundsException {
                return responses.get(num);
            }

            @Override
            public List<Optional<Rs>> response() {
                return responses;
            }
        };
    }

    private List<Optional<Rs>> runTasks() {
        List<Optional<Rs>> responses = new ArrayList<>();
        for (DictionaryTask<Rq, Rs> task : tasks) {
            responses.add(task.noConcurrentCall(cacheDictionaryService));
        }
        return responses;
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        for (LocalCachedTable localCachedTable : tables.values()) {
            final Optional<List<Object>> all = service.all(localCachedTable.table());
            localCachedTable.loadData(all.or(new ArrayList<>()));
        }
        this.cacheDictionaryService = new LocalCacheDictionaryService(tables);
    }
}
