package com.gmail.rgizmalkov.job.impl.v1.srv;

import com.gmail.rgizmalkov.job.api.options.Function;
import com.gmail.rgizmalkov.job.api.service.CacheDictionaryService;
import com.gmail.rgizmalkov.job.api.service.CacheServiceRs;
import com.gmail.rgizmalkov.job.entity.TestEntity_1;
import com.gmail.rgizmalkov.job.entity.TestServiceRq_1;
import com.gmail.rgizmalkov.job.entity.TestServiceRs_1;
import com.gmail.rgizmalkov.job.impl.v1.api.row.BaseFilter;
import com.gmail.rgizmalkov.job.impl.v1.api.row.FilterFactory;
import com.gmail.rgizmalkov.job.impl.v1.api.row.LocalCachedTable;
import com.gmail.rgizmalkov.job.mock.FpDictionaryService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

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
        LocalCachedTable<TestEntity_1> table = new LocalCachedTable<>(TestEntity_1.class, Sets.newHashSet("code", "num"));
        service.tables(table);

        ImmutableMap<String, LocalCachedTable> tables = this.<ImmutableMap<String, LocalCachedTable>>getField("tables", service);
        assertEquals(1, tables.size());
    }

    @Test
    public void srvFullCall_1() {
        FpDictionaryService dictionaryService = Mockito.mock(FpDictionaryService.class);

        TestEntity_1 entity_1 = new TestEntity_1("id_1", "code_1", 1, true);
        TestEntity_1 entity_2 = new TestEntity_1("id_2", "code_2", 2, true);
        TestEntity_1 entity_3 = new TestEntity_1("id_3", "code_2", 3, true);
        TestEntity_1 entity_4 = new TestEntity_1("id_4", "code_2", 4, true);
        TestEntity_1 entity_5 = new TestEntity_1("id_5", "code_2", 5, true);
        TestEntity_1 entity_6 = new TestEntity_1("id_6", "code_6", 6, true);
        TestEntity_1 entity_7 = new TestEntity_1("id_7", null, 7, true);
        TestEntity_1 entity_8 = new TestEntity_1("id_8", "code_8", 8, true);
        TestEntity_1 entity_9 = new TestEntity_1("id_9", "code_9", 9, true);

        ArrayList<TestEntity_1> list = Lists.<TestEntity_1>newArrayList(entity_1, entity_2, entity_3, entity_4, entity_5, entity_6, entity_7, entity_8, entity_9);
        doReturn(Optional.of(list)).when(dictionaryService).all(eq(TestEntity_1.class));

        LocalCachedTable<TestEntity_1> table = new LocalCachedTable<>(TestEntity_1.class, Sets.newHashSet("code", "num"));

        LocalSrvCacheService<TestServiceRq_1, TestServiceRs_1> service = new LocalSrvCacheService<>(dictionaryService);

        CacheServiceRs<TestServiceRs_1> response = service.tables(table).index(TestEntity_1.class, "id")
                .task(new TestServiceRq_1(), new Function<TestServiceRq_1, TestServiceRs_1>() {
                    @Override
                    public Optional<TestServiceRs_1> apply(TestServiceRq_1 request, CacheDictionaryService service) {
                        Optional<TestEntity_1> testEntity_1Optional = service.get(
                                BaseFilter.like()
                                TestEntity_1.class
                        );
                        return Optional.of(new TestServiceRs_1());
                    }
                })
                .response();
        assertEquals(1, response.size());
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