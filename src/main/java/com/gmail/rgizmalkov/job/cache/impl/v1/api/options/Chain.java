package com.gmail.rgizmalkov.job.cache.impl.v1.api.options;

import com.gmail.rgizmalkov.job.cache.impl.v1.errors.IllegalAccessToChainElement;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Chain<Link,Transition> {
    private List<Link> chainLinks;
    private List<Transition> chainTransition;

    private int size;
    private int link_pointer;
    private int transition_pointer;

    private Chain(Link link){
        this.chainLinks = Lists.newArrayList(link);
        this.chainTransition = new ArrayList<>();
        this.size = 1;
        this.link_pointer = 0;
        this.transition_pointer = 0;
    }

    public static <Link,Transition>  Chain<Link,Transition> newArrayChain(Link link){
        Preconditions.checkNotNull(link, "First element of chain can not be null.");
        return new Chain<>(link);
    }

    @SafeVarargs
    public static <Link,Transition>  Chain<Link,Transition> newArrayChain(Link initial, Element<Link,Transition> ... elements){
        Preconditions.checkNotNull(initial, "First element of chain can not be null.");
        Chain<Link, Transition> chain = new Chain<>(initial);
        for (Element<Link, Transition> element : elements) {
            chain.link(element.link, element.transition);
        }
        return chain;
    }

    /**
     * Добавить звено цепи. Действие доступно пока не начался перебор цепи
     * @param chainLink - новый элемент цепи
     * @param chainTransition - связь между новым элементом цепи и хвостом
     * @return - цепь
     */
    public Chain<Link, Transition> link(Link chainLink, Transition chainTransition) {
        Preconditions.checkNotNull(chainLink, "Element of chain cannot be null");
        Preconditions.checkNotNull(chainTransition, "Link between chain's elements cannot be null");
        if(link_pointer == 0 && transition_pointer == 0){
            this.chainLinks.add(chainLink);
            this.chainTransition.add(chainTransition);
            size++;
        }
        return this;
    }

    public Link element(){
        if (link_pointer != transition_pointer) {
            throw new IllegalAccessToChainElement("Violation of access rules to chain's elements. Caused by - call method element() sequentially without method transition()");
        }
        return this.chainLinks.get(link_pointer++);
    }

    public Transition transition() {
        if(transition_pointer != (link_pointer-1)){
            throw new IllegalAccessToChainElement("Violation of access rules to chain's elements. Caused by - call method transition() sequentially without method element()");
        }else if (link_pointer == size) {
            throw new ArrayIndexOutOfBoundsException("Listing of chain was rejected. Caused by - all chain elements were scanned.");
        }
        return this.chainTransition.get(transition_pointer++);
    }

    public boolean hasNext() {
        return link_pointer < size;
    }

    public int chainSize() {
        return chainLinks.size();
    }

    @Override
    public String toString() {
        if(chainTransition.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chainTransition.size(); i++) {
                sb.append(chainLinks.get(i)).append(" ").append(chainTransition.get(i)).append(" ");
            }
            return sb.append(chainLinks.get(chainLinks.size() - 1)).toString();
        }else {
            return chainLinks.get(0).toString();
        }
    }

    public String toString(String pattern) {
        if(chainTransition.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chainTransition.size(); i++) {
                sb.append(String.format(pattern, chainLinks.get(i))).append(" ").append(chainTransition.get(i)).append(" ");
            }
            return sb.append(String.format(pattern, chainLinks.get(chainLinks.size() - 1))).toString();
        }else {
            return chainLinks.get(0).toString();
        }
    }

    /**
     * Произвести ряд логических операций сжимая цепь в один объект
     * @param function - функция сжимающая два элемента  цепи в один
     * @return - сжатая цепь - один элемент
     */
    public Link compress(Function<Link,Transition,Link> function){
        Link left = this.element();
        while (this.hasNext()){
            left = function.apply(left, this.transition(), this.element());
        }
        return left;
    }

    public <Result> Result compress(Aggregation<Result,Link> aggregation, Function<Result,Transition,Result> function){
        Result left = aggregation.transform(this.element());
        while (this.hasNext()){
            left = function.apply(left, this.transition(), aggregation.transform(this.element()));
        }
        return left;
    }

    public interface Aggregation<L,R>{
        L transform(R source);
    }
    public interface Function<L,A,R>{
        L apply(L left, A action, R right);
    }

    public static class Element<Link,Transition>{
        private Link link;
        private Transition transition;

        public static <Link, Transition> Element<Link, Transition> of(Link link, Transition transition) {
            Element<Link, Transition> element = new Element<>();
            element.link = link;
            element.transition = transition;
            return element;
        }

        public Link getLink() {
            return link;
        }

        public Transition getTransition() {
            return transition;
        }
    }
}
