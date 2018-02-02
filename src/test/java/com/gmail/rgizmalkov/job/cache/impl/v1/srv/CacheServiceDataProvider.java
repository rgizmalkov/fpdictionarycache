package com.gmail.rgizmalkov.job.cache.impl.v1.srv;

import com.gmail.rgizmalkov.job.entity.TestEntity_1;
import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

public class CacheServiceDataProvider {

    @DataProvider(name="dataSet_001")
    public Object[][] pack_1(){
        return new Object[][]{
                {set(set_1(),set_2(),set_3(),set_4(),set_5(),set_6(),set_7(),set_8(),set_9(),set_10(),set_11(),set_12())}
        };
    }

    private Map<String, TestEntity_1> set(TestEntity_1 ... list){
        Map<String, TestEntity_1> set = new HashMap<>();
        for (TestEntity_1 entity : list) {
            set.put(entity.getId(), entity);
        }
        return set;
    }

    private TestEntity_1 set_1(){
        return new TestEntity_1("id_1", "code_1", 1, true);
    }
    private TestEntity_1 set_2(){
        return new TestEntity_1("id_2", "code_2", 2, true);
    }
    private TestEntity_1 set_3(){
        return  new TestEntity_1("id_3", "code_3", 3, true);
    }
    private TestEntity_1 set_4(){
        return  new TestEntity_1("id_4", "code_4", 4, true);
    }
    private TestEntity_1 set_5(){
        return  new TestEntity_1("id_5", "code_5", 5, true);
    }
    private TestEntity_1 set_6(){
        return new TestEntity_1("id_6", "code_6", 6, true);
    }
    private TestEntity_1 set_7(){
        return new TestEntity_1("id_7", null, 7, true);
    }
    private TestEntity_1 set_8(){
        return  new TestEntity_1("id_8", "code_8", 8, true);
    }
    private TestEntity_1 set_9(){
        return  new TestEntity_1("id_9", "code_9", 9, true);
    }
    private TestEntity_1 set_10(){
        return new TestEntity_1("id_3", "code_2", 3, true);
    }
    private TestEntity_1 set_11(){
        return new TestEntity_1("id_4", "code_2", 4, true);
    }
    private TestEntity_1 set_12(){
        return new TestEntity_1("id_5", "code_2", 5, true);
    }
}
