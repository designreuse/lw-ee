package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.FreightDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;

import javax.persistence.EntityManager;

/**
 * Created by Nikolay on 23.11.2015.
 */
@Repository("freightDAO")
public class FreightDAOImpl extends AbstractDAOImpl<Freight> implements FreightDAO {


}
