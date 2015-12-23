package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.Query;
import java.util.List;

/**
 * Operations for Truck entity (implementation).
 *
 * @author Nikolay Kuzmenkov.
 */
@Repository("truckDAO")
public class TruckDAOImpl extends AbstractDAOImpl<Truck> implements TruckDAO {

    private static final Logger LOGGER = Logger.getLogger(TruckDAOImpl.class);

    @Override //
    public List<Truck> findByMinCapacityWhereStatusOkAndNotAssignedToOrder(Float minCargoCapacity) throws LogiwebDAOException {
        try {
            Query query = getEntityManager().createQuery("SELECT tr FROM Truck tr WHERE tr.truckStatus = :status  " +
                    "AND tr.orderForThisTruck IS NULL AND tr.capacity >= :capacity", Truck.class);
            query.setParameter("status", TruckStatus.WORKING);
            query.setParameter("capacity", minCargoCapacity);

            @SuppressWarnings("unchecked")
            List<Truck> queryResult = query.getResultList();

            return queryResult;

        } catch (Exception e) {
            LOGGER.warn("Exception in TruckDAOImpl - findByMinCapacityWhereStatusOkAndNotAssignedToOrder()", e);
            throw new LogiwebDAOException(e);
        }
    }

    @Override
    public Truck findTruckByTruckNumber(String truckNumber) throws LogiwebDAOException {
        try {
            Query query = getEntityManager().createQuery("SELECT t FROM Truck t WHERE t.truckNumber = :truckNumber",
                    Truck.class);
            query.setParameter("truckNumber", truckNumber);
            @SuppressWarnings("unchecked")
            List<Truck> resultList = query.getResultList();

            if(resultList.isEmpty()) {
                return null;
            } else {
                return resultList.get(0);
            }

        } catch(Exception e) {
            LOGGER.warn("Exception in TruckDAOImpl - findTruckByTruckNumber().", e);
            throw new LogiwebDAOException(e);
        }
    }
}
