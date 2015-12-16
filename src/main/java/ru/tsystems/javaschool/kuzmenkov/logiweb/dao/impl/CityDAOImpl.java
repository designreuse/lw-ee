package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;

import javax.persistence.EntityManager;

/**
 * Created by Nikolay on 27.11.2015.
 */
@Repository("cityDAO")
public class CityDAOImpl extends AbstractDAOImpl<City> implements CityDAO {

}
