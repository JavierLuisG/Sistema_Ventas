package com.proyect.sistemaventas.dal.dao;

import java.util.List;

public interface SystemDAO<T> {

    int insert(T t);

    int update(T t);

    int delete(T t);

    int findById(T t);

    List<T> findAll(T t);
}
