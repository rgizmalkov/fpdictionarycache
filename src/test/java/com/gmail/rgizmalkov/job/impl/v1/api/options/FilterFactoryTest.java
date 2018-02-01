package com.gmail.rgizmalkov.job.impl.v1.api.options;

import com.gmail.rgizmalkov.job.impl.v1.api.row.Filter;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class FilterFactoryTest {

    @Test
    public void testComplexFilter() {
        Chain<FilterFactory, Filter.Action> filter = FilterFactory
                .and(
                        Filter.eq("", ""),
                        Filter.eq("", ""))
                .or(Filter.eq("", ""))
                .and(
                        FilterFactory.and(
                                Filter.eq("", ""),
                                Filter.eq("", "")
                        ))
                .filter();

        assertEquals(2, filter.chainSize());

        Chain<FilterFactory, Filter.Action> filter2 = FilterFactory
                .and(
                        Filter.eq("", ""),
                        Filter.eq("", ""))
                .or(Filter.eq("", ""))
                .filter();

        assertEquals(1, filter2.chainSize());

    }
}
