package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.AbstractDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Abstract class with set of basic CRUD operations.
 *
 * @author Nikolay Kuzmenkov.
 */
public class AbstractDAOImpl<T> implements AbstractDAO<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractDAOImpl.class);

    private Class<T> entityClass;
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractDAOImpl() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected final EntityManager getEntityManager() {
        return entityManager;
    }

    protected final Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Persist new object into database.
     *
     * @param newEntity
     * @return same persistent object.
     * @throws LogiwebDAOException if failed to persist object.
     */
    @Override
    public T create(T newEntity) throws LogiwebDAOException {
        try {
            entityManager.persist(newEntity);

            return newEntity;

        } catch (PersistenceException e) {
            LOGGER.warn("Exception in AbstractDAOImpl - create().", e);
            throw new LogiwebDAOException(e);
        }


    }

    /**
     * Find persistent object by entity ID (primary key).
     *
     * @param entityId
     * @return persistent object or null if not found.
     * @throws LogiwebDAOException if failed to find entity by ID.
     */
    @Override
    public T findById(Integer entityId) throws LogiwebDAOException { //
        try {
             return entityManager.find(getEntityClass(), entityId);

        } catch (PersistenceException e) {
            LOGGER.warn("Exception in AbstractDAOImpl - findById().", e);
            throw new LogiwebDAOException(e);
        }
    }

    /**
     * Update persistent object.
     *
     * @param changeableEntity persistent object.
     */
    @Override
    public void update(T changeableEntity) throws LogiwebDAOException {
        if (changeableEntity == null) {
            return;
        }

        try {
            entityManager.merge(changeableEntity);

        } catch (PersistenceException e) {
            LOGGER.warn("Exception in AbstractDAOImpl - update().", e);
            throw new LogiwebDAOException(e);
        }
    }

    /**
     * Delete persistent object from database.
     *
     * @param deletedEntity persistent object.
     */
    @Override
    public void delete(T deletedEntity) throws LogiwebDAOException {
        try {
            entityManager.remove(deletedEntity);

        } catch (PersistenceException e) {
            LOGGER.warn("Exception in AbstractDAOImpl - delete().", e);
            throw new LogiwebDAOException(e);
        }
    }

    /** //
     * Find all objects of that persistent class.
     *
     * @return list of objects or empty list.
     * @throws LogiwebDAOException if failed to find all entities.
     */
    @Override
    public List<T> findAll() throws LogiwebDAOException { //
        try {
            @SuppressWarnings("unchecked")
            List<T> allEntitiesResult = entityManager.createQuery("SELECT t FROM "
                    + entityClass.getSimpleName() + " t").getResultList();

            return allEntitiesResult;

        } catch (PersistenceException e) {
            LOGGER.warn("Exception in AbstractDAOImpl - findAll().", e);
            throw new LogiwebDAOException(e);
        }
    }
}
