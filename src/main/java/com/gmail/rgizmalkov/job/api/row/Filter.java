package com.gmail.rgizmalkov.job.api.row;

public interface Filter {

    Filter and(String key, Object value);
    Filter and(Filter filter);

    Filter or(String key, Object value);
    Filter or(Filter filter);
}
