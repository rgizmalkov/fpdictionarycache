package com.gmail.rgizmalkov.job.api.row;

import com.gmail.rgizmalkov.job.impl.v1.api.options.Chain;
import com.gmail.rgizmalkov.job.impl.v1.api.options.FilterFactory;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter;

public interface OuterFilterFactory {

    OuterFilterFactory or(FilterFactory factory);
    OuterFilterFactory and(FilterFactory factory);
    Chain<FilterFactory, Filter.Action> filter();
}
