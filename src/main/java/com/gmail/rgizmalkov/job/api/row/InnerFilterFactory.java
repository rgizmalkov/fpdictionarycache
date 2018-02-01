package com.gmail.rgizmalkov.job.api.row;

import com.gmail.rgizmalkov.job.impl.v1.api.options.Chain;
import com.gmail.rgizmalkov.job.impl.v1.api.options.FilterFactory;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter;

public interface InnerFilterFactory {

    FilterFactory or(Filter filter);
    FilterFactory and(Filter filter);
    Chain<FilterFactory, Filter.Action> filter();
}
