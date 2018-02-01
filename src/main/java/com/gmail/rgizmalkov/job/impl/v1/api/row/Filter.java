package com.gmail.rgizmalkov.job.impl.v1.api.row;

import com.gmail.rgizmalkov.job.impl.v1.api.options.Chain;
import com.google.common.base.Preconditions;

/**
 * Фильтр для компоновки одного условия.
 * Компоновка производится через логический оператор И (AND).
 */
public class Filter {
    private Chain<Operation,Action> chain;

    private Filter(Operation operation){
        this.chain = Chain.newArrayChain(operation);
    }

    public static <T> Filter eq(String key, T element){
        Preconditions.checkNotNull(key, "Key of filter cannot be null");
        Preconditions.checkNotNull(element, "Element of filter cannot be null");
        return new Filter(new Operation<T>(key, element, Condition.EQ));
    }
    public static Filter like(String key, String pattern){
        Preconditions.checkNotNull(key, "Key of filter cannot be null");
        Preconditions.checkNotNull(pattern, "Pattern of like-filter cannot be null");
        return new Filter(new Operation<String>(key, pattern, Condition.LIKE));
    }
    public <T> Filter addEqCondition(String key, T element) {
        Preconditions.checkNotNull(key, "Key of filter cannot be null");
        Preconditions.checkNotNull(element, "Element of filter cannot be null");
        chain.link(new Operation<>(key, element, Condition.EQ), Action.AND);
        return this;
    }
    public Filter addLikeCondition(String key, String pattern) {
        Preconditions.checkNotNull(key, "Key of filter cannot be null");
        Preconditions.checkNotNull(pattern, "Pattern of like-filter cannot be null");
        chain.link(new Operation<String>(key, pattern, Condition.LIKE), Action.AND);
        return this;
    }

    public Chain<Operation, Action> condition() {
        return this.chain;
    }

    public enum Condition {
        EQ, LIKE
    }
    public enum Action{
        AND, OR
    }
    public static class Operation<T>{
        private String key;
        private T object;
        private Condition condition;

        Operation(String key, T object, Condition condition) {
            this.key = key;
            this.object = object;
            this.condition = condition;
        }

        public String getKey() {
            return key;
        }

        public Operation setKey(String key) {
            this.key = key;
            return this;
        }

        public T getObject() {
            return object;
        }

        public Operation setObject(T object) {
            this.object = object;
            return this;
        }

        public Condition getCondition() {
            return condition;
        }

        public Operation setCondition(Condition condition) {
            this.condition = condition;
            return this;
        }
    }
}
