package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.api.row.Filter;

import java.util.ArrayList;
import java.util.List;

public class BaseFilter implements Filter {
    public enum FilterCurrent {
        LIKE, EQ;
    }
    public class FilterContent<O> {
        String key;
        O value;
        FilterCurrent action;

        FilterContent(String key, O value, FilterCurrent action) {
            this.key = key;
            this.value = value;
            this.action = action;
        }

        public String getKey() {
            return key;
        }

        public FilterContent setKey(String key) {
            this.key = key;
            return this;
        }

        public O getValue() {
            return value;
        }

        public FilterContent setValue(O value) {
            this.value = value;
            return this;
        }

        public FilterCurrent getAction() {
            return action;
        }

        public FilterContent setAction(FilterCurrent action) {
            this.action = action;
            return this;
        }
    }

    private final List<List<FilterContent>> orlist = new ArrayList<>();
    private List<FilterContent> content = new ArrayList<>();

    public Filter or(BaseFilter second, BaseFilter ... filters){
        this.orlist.add(content);
        this.content = new ArrayList<>();
        this.orlist.addAll(second.orlist);
        for (BaseFilter filter : filters) {
            this.orlist.addAll(filter.orlist);
        }
        return this;
    }

    public <O> Filter and(FilterCurrent filterCurrent, String key, O object){
        this.content.add(new FilterContent<O>(key, object, filterCurrent));
        return this;
    }

    @Override
    public Filter and(BaseFilter second, BaseFilter... filters) {
        this.content.addAll(second.content);
        for (BaseFilter filter : filters) {
            this.content.addAll(filter.content);
        }
        return this;
    }

    @Override
    public List<List<FilterContent>> filter() {
        this.orlist.add(this.content);
        return this.orlist;
    }

}
