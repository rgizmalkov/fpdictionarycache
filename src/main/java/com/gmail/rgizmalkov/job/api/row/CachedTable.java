package com.gmail.rgizmalkov.job.api.row;

import com.gmail.rgizmalkov.job.impl.v1.api.row.LocalCachedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.List;

public interface CachedTable<Type> extends Table<Type> {

    /**
     * Текущее состояние таблицы
     */
    List<Type> content();

    /**
     * Поиск списка объектов эквивалентных значению заданного столбц
     * @param column - название стобца
     * @param object - объект с которым сравнивают
     * @param <O> - тип обхекта с которым сравнивают
     * @return - таблица с сокращенной выборкой
     */
    <O> LocalCachedTable newTableFrom(String column, O object);

    /**
     * Поиск списка объектов эквивалентных значению заданного столбца
     * @param column - название стобца
     * @param object - объект с которым сравнивают
     * @param <O> - тип обхекта с которым сравнивают
     * @return - список соответсвующих объектов
     */
    <O> List<Type> find(String column, O object);

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

