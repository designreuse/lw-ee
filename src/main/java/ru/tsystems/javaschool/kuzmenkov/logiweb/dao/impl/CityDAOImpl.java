package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;

import javax.persistence.EntityManager;

/**
 * Created by Nikolay on 27.11.2015.
 */
public class CityDAOImpl extends AbstractDAOImpl<City> implements CityDAO {

    public CityDAOImpl(Class<City> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }
}
