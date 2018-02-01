package com.gmail.rgizmalkov.job.entity;

import com.google.common.base.MoreObjects;

import java.util.List;

public class TestServiceRs_1 {
    List<TestEntity_1> entities;

    public TestServiceRs_1(List<TestEntity_1> entities) {
        this.entities = entities;
    }

    public List<TestEntity_1> getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entities", entities)
                .toString();
    }
}
