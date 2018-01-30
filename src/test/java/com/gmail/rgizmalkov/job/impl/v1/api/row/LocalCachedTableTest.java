package com.gmail.rgizmalkov.job.impl.v1.api.row;



import com.gmail.rgizmalkov.job.entity.TestEntity_1;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalCachedTableTest {

    @Test
    public void createTable() {
        LocalCachedTable<TestEntity_1> table = new LocalCachedTable<>(TestEntity_1.class, Sets.newHashSet("code", "num"));

        assertTrue(table.isPresent("code"));
        assertFalse(table.isPresent("num"));
    }

    @Test
    public void loadData_1() {
        LocalCachedTable<TestEntity_1> table = new LocalCachedTable<>(TestEntity_1.class, Sets.newHashSet("code", "num"));

        TestEntity_1 entity_1 = new TestEntity_1("id_1", "code_1", 1, true);
        TestEntity_1 entity_2 = new TestEntity_1("id_2", "code_2", 2, true);
        TestEntity_1 entity_3 = new TestEntity_1("id_3", "code_3", 3, true);
        TestEntity_1 entity_4 = new TestEntity_1("id_4", "code_4", 4, true);
        TestEntity_1 entity_5 = new TestEntity_1("id_5", "code_5", 5, true);
        TestEntity_1 entity_6 = new TestEntity_1("id_6", "code_6", 6, true);
        TestEntity_1 entity_7 = new TestEntity_1("id_7", null, 7, true);
        TestEntity_1 entity_8 = new TestEntity_1("id_8", "code_8", 8, true);
        TestEntity_1 entity_9 = new TestEntity_1("id_9", "code_9", 9, true);

        table.loadData(Lists.<TestEntity_1>newArrayList(entity_1, entity_2, entity_3, entity_4, entity_5, entity_6, entity_7, entity_8, entity_9));

        assertTrue(table.isPresent("code"));
        assertFalse(table.isPresent("num"));
    }

}
