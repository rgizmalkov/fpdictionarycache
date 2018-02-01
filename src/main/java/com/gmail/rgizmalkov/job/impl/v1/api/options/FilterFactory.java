package com.gmail.rgizmalkov.job.impl.v1.api.options;

import com.gmail.rgizmalkov.job.api.row.InnerFilterFactory;
import com.gmail.rgizmalkov.job.api.row.OuterFilterFactory;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter;
import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter.Action;
import com.google.common.base.Preconditions;

public class FilterFactory implements InnerFilterFactory, OuterFilterFactory{
    private Chain<FilterFactory, Action> chain;
    private final Chain<Filter, Action> operations;

    private FilterFactory(Filter first, Filter second, Action action) {
        this.operations = Chain.<Filter, Action>newArrayChain(first).link(second, action);
    }


    public static FilterFactory or(Filter first, Filter second) {
        Preconditions.checkNotNull(first, "Filter cannot be null");
        Preconditions.checkNotNull(second, "Filter cannot be null");
        return new FilterFactory(first, second, Action.OR);
    }

    public static FilterFactory and(Filter first, Filter second) {
        Preconditions.checkNotNull(first, "Filter cannot be null");
        Preconditions.checkNotNull(second, "Filter cannot be null");
        return new FilterFactory(first, second, Action.AND);
    }

    public FilterFactory or(Filter filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null");
        this.operations.link(filter, Action.OR);
        return this;
    }

    public FilterFactory and(Filter filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null");
        this.operations.link(filter, Action.AND);
        return this;
    }

    public OuterFilterFactory or(FilterFactory factory) {
        if (chain != null) {
            this.chain.link(factory, Action.OR);
        } else {
            this.chain = Chain.<FilterFactory, Action>newArrayChain(this).link(factory, Action.OR);
        }
        return this;
    }


    public OuterFilterFactory and(FilterFactory factory) {
        if (chain != null) {
            this.chain.link(factory, Action.AND);
        } else {
            this.chain = Chain.<FilterFactory, Action>newArrayChain(this).link(factory, Action.AND);
        }
        return this;
    }

    public Chain<FilterFactory, Action> filter() {
        if(chain == null || chain.chainSize() < 1){
            return Chain.newArrayChain(this);
        }
        return chain;
    }

}
