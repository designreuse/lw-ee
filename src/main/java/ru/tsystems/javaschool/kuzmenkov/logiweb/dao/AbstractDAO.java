package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * DAO interface for basic CRUD operations.
 *
 * @author Nikolay Kuzmenkov.
 * @param <T> persistent object.
 */
public interface AbstractDAO<T> {

    /**
     * Persist new object into database.
     *
     * @param newEntity
     * @return same persistent object.
     * @throws LogiwebDAOException if failed to persist object.
     */
    T create(T newEntity) throws LogiwebDAOException;

    /**
     * Find persistent object by entity ID (primary key).
     *
     * @param entityId
     * @return persistent object or null if not found.
     * @throws LogiwebDAOException if failed to find entity by ID.
     */
    T findById(Integer entityId) throws LogiwebDAOException;

    /**
     * Update persistent object.
     *
     * @param changeableEntity persistent object.
     */
    void update(T changeableEntity) throws LogiwebDAOException;

    /**
     * Delete persistent object from database.
     *
     * @param deletedEntity persistent object.
     */
    void delete(T deletedEntity) throws LogiwebDAOException;

    /**
     * Find all objects of that persistent class.
     *
     * @return list of objects or empty list.
     * @throws LogiwebDAOException if failed to find all entities.
     */
    List<T> findAll() throws LogiwebDAOException;
}
