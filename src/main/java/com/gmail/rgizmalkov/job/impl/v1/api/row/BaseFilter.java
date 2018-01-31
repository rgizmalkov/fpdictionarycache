package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.api.row.Filter;
import com.gmail.rgizmalkov.job.impl.v1.srv.LocalCacheDictionaryService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

public class BaseFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(LocalCacheDictionaryService.class);

    private final List<List<FilterContent>> orlist = Lists.newArrayList(new ArrayList<>());

    public Filter or(BaseFilter second, BaseFilter ... filters){
        this.orlist.addAll(second.orlist);
        for (BaseFilter filter : filters) {
            this.orlist.addAll(filter.orlist);
        }
        this.orlist.add(new ArrayList<>());
        return this;
    }

    public <O> Filter and(FilterCurrent filterCurrent, String key, O object){
        int place = orlist.size() - 1;
        this.orlist.get(place).add(new FilterContent<O>(key, object, filterCurrent));
        return this;
    }

    @Override
    public Filter and(BaseFilter second, BaseFilter... filters) {
        List<List<FilterContent>> orlist = second.orlist;
        return this;
    }

    @Override
    public List<List<FilterContent>> filter() {
        return this.orlist;
    }


    public static BaseFilter eq(
            String column, String value
    ){
        Preconditions.checkNotNull(column, "Key have to be not null!");
        Preconditions.checkNotNull(value, "Object have to be not null!");
        return eq(new BaseFilter(), column, value);
    }

    public static <O>  BaseFilter eq(
            Filter filter, String column, O value
    ){
        Preconditions.checkNotNull(column, "Key have to be not null!");
        Preconditions.checkNotNull(value, "Object have to be not null!");
        return (BaseFilter) filter.<O>and(BaseFilter.FilterCurrent.EQ, column, value);
    }

    @Override
    public String toString() {
        List<String> joinedAndConditions = new ArrayList<>();
        for (List<FilterContent> filterContents : orlist) {
            joinedAndConditions.add("(" + join(filterContents, " AND ") + ")");
        }
        return join(joinedAndConditions, " OR ");
    }

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

        @Override
        public String toString() {
            switch (action){
                case EQ:
                    return key + "=\'" + String.valueOf(value) + "\'";
                case LIKE:
                    return key + "LIKE \'" + String.valueOf(value) + "\'";
            }
            return "";
        }
    }
}
