package com.proyect.sistemaventas.dal.dao;

/**
 * @param <T> interfaz genérica que define los métodos básicos para interactuar
 * con la base de datos
 */
public interface LoginDAO<T> {

    int findByEmailAndPassword(T t);
}
