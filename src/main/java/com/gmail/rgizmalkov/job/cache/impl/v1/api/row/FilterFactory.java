package com.gmail.rgizmalkov.job.cache.impl.v1.api.row;

import com.gmail.rgizmalkov.job.cache.impl.v1.api.options.Chain;
import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.Filter.Action;
import com.google.common.base.Preconditions;

public class FilterFactory {
    private final Chain<Filter, Action> chain;

    private FilterFactory(Filter filter_1, Action action, Filter filter_2) {
        this.chain = Chain.newArrayChain(filter_1, Chain.Element.of(filter_2, action));
    }

    public static FilterFactory or(Filter first, Filter second, Filter... other) {
        return action(Action.OR, first, second, other);
    }
    public static FilterFactory and(Filter first, Filter second, Filter... other) {
        return action(Action.AND, first, second, other);
    }

    private static FilterFactory action(Action action, Filter first, Filter second, Filter... other) {
        Preconditions.checkNotNull(first, "Filter can not be null");
        Preconditions.checkNotNull(second, "Filter can not be null");
        FilterFactory factory = new FilterFactory(first, action, second);
        if (other != null && other.length > 0) {
            for (Filter filter : other) {
                factory.chain.link(filter, action);
            }
        }
        return factory;
    }

    public Chain<Filter, Action> chain() {
        return chain;
    }

    @Override
    public String toString() {
        return chain.toString("(%s)");
    }
}
