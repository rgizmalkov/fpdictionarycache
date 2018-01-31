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

    public static Filter like(String key, String value) {
        return null;
    }




}
