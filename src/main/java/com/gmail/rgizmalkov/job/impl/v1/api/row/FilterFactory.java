package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.api.row.Filter;
import com.google.common.base.Preconditions;

public final class FilterFactory {

    private FilterFactory(){}


    public static Filter or(
            Filter first, BaseFilter second, BaseFilter ... other
    ){
        if(first != null && second != null){
            if(other != null){
                return first.or(second, other);
            }else
                return first.or(second);
        }
        return first;
    }

    public static Filter and(
            Filter first, BaseFilter second, BaseFilter ... other
    ){
        if(first != null && second != null){
            if(other != null){
                return first.and(second, other);
            }else
                return first.and(second);
        }
        return first;
    }

    public static BaseFilter eq(
            String key, String value
    ){
        Preconditions.checkNotNull(key, "Key have to be not null!");
        Preconditions.checkNotNull(value, "Object have to be not null!");
        return eq(new BaseFilter(), key, value);
    }

    public static <O>  BaseFilter eq(
            Filter filter, String key, O value
    ){
        Preconditions.checkNotNull(key, "Key have to be not null!");
        Preconditions.checkNotNull(value, "Object have to be not null!");
        return (BaseFilter) filter.<O>and(BaseFilter.FilterCurrent.EQ, key, value);
    }

    public static Filter like(String key, String value) {
        return null;
    }




}
