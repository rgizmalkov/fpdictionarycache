package com.gmail.rgizmalkov.job.cache.impl.v1.srv;

import com.gmail.rgizmalkov.job.cache.api.options.Task;
import com.gmail.rgizmalkov.job.cache.api.service.CacheDictionaryService;
import com.gmail.rgizmalkov.job.cache.api.service.CacheServiceRs;
import com.gmail.rgizmalkov.job.entity.TestEntity_1;
import com.gmail.rgizmalkov.job.entity.TestServiceRq_1;
import com.gmail.rgizmalkov.job.entity.TestServiceRs_1;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.Filter;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.FilterFactory;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.LocalCachedTable;
import com.gmail.rgizmalkov.job.mock.FpDictionaryService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("all")
public class LocalSrvCacheServiceTest {
    private final static Logger logger = LoggerFactory.getLogger(LocalSrvCacheServiceTest.class);

    @InjectMocks
    private LocalSrvCacheService<TestServiceRq_1, TestServiceRs_1> service;

    @Mock
    private FpDictionaryService dictionaryService;

    @Mock
    private CacheDictionaryService cacheDictionaryService;

    @BeforeClass
    public void init() {
        initMocks(this);
    }


    @Test
    public void srvCreateTables_1() {
        LocalCachedTable<TestEntity_1> table = LocalCachedTable.of(TestEntity_1.class, "code", "num");
        service.tables(table);

        ImmutableMap<String, LocalCachedTable> tables = this.<ImmutableMap<String, LocalCachedTable>>getField("tables", service);
        assertEquals(1, tables.size());
    }

    @Test(dataProvider = "dataSet_001", dataProviderClass = CacheServiceDataProvider.class)
    public void srvFullCall_1(Map<String,TestEntity_1> set) {
        FpDictionaryService dictionaryService = Mockito.mock(FpDictionaryService.class);
        doReturn(Optional.of(Lists.newArrayList(set.values()))).when(dictionaryService).all(eq(TestEntity_1.class));
        LocalCachedTable<TestEntity_1> table = LocalCachedTable.of(TestEntity_1.class, "code", "num");

        LocalSrvCacheService<TestServiceRq_1, TestServiceRs_1> service = new LocalSrvCacheService<>(dictionaryService);
        CacheServiceRs<TestServiceRs_1> response = service.tables(table).task(new TestServiceRq_1(TestEntity_1.class), new Task<TestServiceRq_1, TestServiceRs_1>() {
            public Optional<TestServiceRs_1> apply(TestServiceRq_1 request, CacheDictionaryService service) {
                Optional<TestEntity_1> optional = service.<TestEntity_1>get(Filter.eq("id", "id_9"), request.getTable());
                return Optional.of(new TestServiceRs_1(Lists.newArrayList(optional.get())));
            }
        }).response();

        System.out.println(response.response());
        assertEquals(response.response().get(0).get().getEntities().size(), 1);
    }

    @Test(dataProvider = "dataSet_001", dataProviderClass = CacheServiceDataProvider.class)
    public void srvFullCall_2(Map<String,TestEntity_1> set) {
        FpDictionaryService dictionaryService = Mockito.mock(FpDictionaryService.class);
        doReturn(Optional.of(Lists.newArrayList(set.values()))).when(dictionaryService).all(eq(TestEntity_1.class));
        LocalCachedTable<TestEntity_1> table = LocalCachedTable.of(TestEntity_1.class, "code", "num");

        LocalSrvCacheService<TestServiceRq_1, TestServiceRs_1> service = new LocalSrvCacheService<>(dictionaryService);
        CacheServiceRs<TestServiceRs_1> response = service.tables(table).task(new TestServiceRq_1(TestEntity_1.class), new Task<TestServiceRq_1, TestServiceRs_1>() {
            public Optional<TestServiceRs_1> apply(TestServiceRq_1 request, CacheDictionaryService service) {
                Optional<List<TestEntity_1>> listOptional = service.<TestEntity_1>few(
                        FilterFactory.or(
                                Filter.eq("id", "id_4"),
                                Filter.like("code", "code_.*$").addEqCondition("id", "id_3")
                        ),
                        request.getTable()
                );
                return Optional.of(new TestServiceRs_1(Lists.newArrayList(listOptional.get())));
            }
        }).response();

        System.out.println(response.response());
        assertEquals(response.response().get(0).get().getEntities().size(), 2);
    }


    private <T> T getField(String code, Object object) {
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(code);
            ReflectionUtils.makeAccessible(field);
            return ((T) ReflectionUtils.getField(field, object));
        } catch (Exception ex) {
            logger.warn("Cannot get field");
            return null;
        }
    }



}