package com.gmail.rgizmalkov.job.cache.api.row;

import com.gmail.rgizmalkov.job.cache.impl.v1.api.row.LocalCachedTable;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public interface CachedTable<Type> extends Table<Type> {

    /**
     * Текущее состояние таблицы
     */
    List<Type> content();

    /**
     * Поиск списка объектов эквивалентных значению заданного столбца
     * @param column - название стобца
     * @param object - объект с которым сравнивают
     * @param <O> - тип обхекта с которым сравнивают
     * @return - таблица с сокращенной выборкой
     */
    <O> LocalCachedTable findNewTableFrom(String column, O object);

    /**
     * Поиск списка объектов эквивалентных значению заданного столбца
     * @param column - название стобца
     * @param object - объект с которым сравнивают
     * @param <O> - тип обхекта с которым сравнивают
     * @return - список соответсвующих объектов
     */
    <O> ImmutableMap<String,Type> find(String column, O object);

    /**
     * Поиск списка объектов подходящих по шаблону значению заданного столбца
     * @param column - название стобца
     * @param pattern - объект с которым сравнивают
     * @param <O> - тип обхекта с которым сравнивают
     * @return - таблица с сокращенной выборкой
     */
    <O> LocalCachedTable likeNewTableFrom(String column, String pattern);

    /**
     * Поиск списка объектов подходящих по шаблону значению заданного столбца
     * @param column - название стобца
     * @param pattern - объект с которым сравнивают
     * @param <O> - тип обхекта с которым сравнивают
     * @return - список соответсвующих объектов
     */
    <O> ImmutableMap<String,Type> like(String column, String pattern);



    /**
     * Наполнение таблицы данными
     * @param objects - список объектов
     * @return - таблица
     */
    CachedTable<Type> loadData(List<Type> objects);

    /**
     * Получить доступ к индексу таблицы или null
     * @param indexCode - код индекса
     * @return - индекс или null
     */
    Index get(String indexCode);

    /**
     * Признак существования индекса в таблице
     * @param indexCode - код индекса
     * @return - true/false
     */
    boolean isPresent(String indexCode);

    /**
     * Добавить индекс в таблицу
     * @param indexCode - название столбца таблицы
     * @return - таблица
     */
    CachedTable<Type> addIndex(String indexCode);

    /**
     * Удалить индекс из таблицы
     * @param indexCode - название столбца таблицы
     * @return - таблица
     */
    CachedTable<Type> dropIndex(String indexCode);


}

