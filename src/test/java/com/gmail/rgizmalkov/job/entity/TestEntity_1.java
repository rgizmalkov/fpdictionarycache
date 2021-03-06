package com.gmail.rgizmalkov.job.entity;


import com.gmail.rgizmalkov.job.mock.Dictionary;
import com.google.common.base.MoreObjects;

import java.util.Objects;

@Dictionary
public class TestEntity_1 {
    private String id;
    private String code;
    private Integer num;
    private Boolean sign;

    public TestEntity_1() {
    }

    public TestEntity_1(String id, String code, Integer num, Boolean sign) {
        this.id = id;
        this.code = code;
        this.num = num;
        this.sign = sign;
    }

    public String getId() {
        return id;
    }

    public TestEntity_1 setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TestEntity_1 setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public TestEntity_1 setNum(Integer num) {
        this.num = num;
        return this;
    }

    public Boolean getSign() {
        return sign;
    }

    public TestEntity_1 setSign(Boolean sign) {
        this.sign = sign;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("code", code)
                .add("num", num)
                .add("sign", sign)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestEntity_1)) return false;
        TestEntity_1 entity_1 = (TestEntity_1) o;
        return Objects.equals(getId(), entity_1.getId()) &&
                Objects.equals(getCode(), entity_1.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode());
    }
}
