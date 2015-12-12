package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Nikolay on 18.11.2015.
 */
public class LogiwebAppResources {

    private static class SingletonHolder {
        private static final LogiwebAppResources SINGLETON_INSTANCE = new LogiwebAppResources();
    }

    private static final String PERSISTENCE_UNIT = "logiwebPU";

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private LogiwebAppResources() {

    }

    public static LogiwebAppResources getInstance() {
        return SingletonHolder.SINGLETON_INSTANCE;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if(entityManagerFactory == null) {
            synchronized(this) {
                if(entityManagerFactory == null) {
                    entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                }
            }
        }

        return entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        if(entityManager == null || !entityManager.isOpen()) {
            synchronized(this) {
                if(entityManager == null || !entityManager.isOpen()) {
                    entityManager = getEntityManagerFactory().createEntityManager();
                }
            }
        }

        return entityManager;
    }

    private DriverDAO getDriverDAO() {
        return new DriverDAOImpl();
    }

    private DriverShiftDAO getDriverShiftDAO() {
        return new DriverShiftDAOImpl(DriverShift.class, getEntityManager());
    }

    public DriverService getDriverService() {
        return new DriverServiceImpl(getDriverShiftDAO(), getTruckDAO(), getEntityManager());
    }

    private TruckDAO getTruckDAO() {
        return new TruckDAOImpl(Truck.class, getEntityManager());
    }

    public TruckService getTruckService() {
        return new TruckServiceImpl(getDriverDAO(),getTruckDAO(), getEntityManager());
    }

    private FreightDAO getFreightDAO() {
        return new FreightDAOImpl(Freight.class, getEntityManager());
    }

    private OrderDAO getOrderDAO() {
        return new OrderDAOImpl(Order.class, getEntityManager());
    }

    public OrderService getOrderService() {
        return new OrderServiceImpl(getCityDAO(), getFreightDAO(), getOrderDAO(), getTruckDAO(), getEntityManager());
    }

    private CityDAO getCityDAO() {
        return new CityDAOImpl(City.class, getEntityManager());
    }

    public CityService getCityService() {
        return new CityServiceImpl(getEntityManager(), getCityDAO());
    }

    private UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    public UserService getUserService() {
        return new UserServiceImpl(getEntityManager(), getUserDAO());
    }
}
