package com.gmail.rgizmalkov.job.api.row;

import com.gmail.rgizmalkov.job.impl.v1.api.row.BaseFilter;

import java.util.List;

public interface Filter {

    Filter or(BaseFilter second, BaseFilter ... filters);

    <O> Filter and(BaseFilter.FilterCurrent filterCurrent, String key, O object);

    Filter and(BaseFilter second, BaseFilter ... filters);

    List<List<BaseFilter.FilterContent>> filter();

}
