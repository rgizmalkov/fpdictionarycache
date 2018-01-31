package com.gmail.rgizmalkov.job.impl.v1.api.options;

import com.gmail.rgizmalkov.job.entity.TestEntity_1;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class MassiveFilterTest {
    @Test
    public void testLike() throws Exception {
        Map<String, String> likeMap = ImmutableMap.of(
                "like_g", "",
                "like_t", "",
                "like_s", "",
                "glike", "",
                "slike", ""
        );

        Map<String, String> map = MassiveFilter.like(likeMap, "^like.*$");
        assertEquals(3, map.size());
    }

    @Test
    public void testLikeValueField(){
        TestEntity_1 entity_1 = new TestEntity_1("id_1", "code_1", 1, true);
        TestEntity_1 entity_2 = new TestEntity_1("id_2", "code_2", 2, true);
        TestEntity_1 entity_3 = new TestEntity_1("id_3", "_code_2", 3, true);
        TestEntity_1 entity_4 = new TestEntity_1("id_4", "_code_2", 4, true);
        TestEntity_1 entity_5 = new TestEntity_1("id_5", "code_2", 5, true);
        TestEntity_1 entity_6 = new TestEntity_1("id_6", "_code_6", 6, true);
        TestEntity_1 entity_7 = new TestEntity_1 ("id_7", null, 7, true);
        TestEntity_1 entity_8 = new TestEntity_1("id_8", "code_8", 8, true);
        TestEntity_1 entity_9 = new TestEntity_1("id_9", "code_9", 9, true);

        Map<String, TestEntity_1> source = new HashMap<>();
        source.put(entity_1.getId(), entity_1);
        source.put(entity_2.getId(), entity_2);
        source.put(entity_3.getId(), entity_3);
        source.put(entity_4.getId(), entity_4);
        source.put(entity_5.getId(), entity_5);
        source.put(entity_6.getId(), entity_6);
        source.put(entity_7.getId(), entity_7);
        source.put(entity_8.getId(), entity_8);
        source.put(entity_9.getId(), entity_9);

        Map<String, TestEntity_1> target = MassiveFilter.likeValueField(source, "^_code.*$", "code");
        assertEquals(3, target.size());

        Map<String, TestEntity_1> target2 = MassiveFilter.likeValueField(source, "^_code.*$", "code1");
        assertEquals(0, target2.size());
    }

}