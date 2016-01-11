package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;

/**
 * @author Nikolay Kuzmenkov.
 */
@Repository("cityDAO")
public class CityDAOImpl extends AbstractDAOImpl<City> implements CityDAO {

}
