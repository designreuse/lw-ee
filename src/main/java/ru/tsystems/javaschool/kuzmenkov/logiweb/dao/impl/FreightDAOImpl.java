package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.FreightDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;

import javax.persistence.EntityManager;

/**
 * Created by Nikolay on 23.11.2015.
 */
public class FreightDAOImpl extends AbstractDAOImpl<Freight> implements FreightDAO {

    public FreightDAOImpl(Class<Freight> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }
}
