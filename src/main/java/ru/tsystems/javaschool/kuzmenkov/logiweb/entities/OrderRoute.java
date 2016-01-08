package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public class OrderRoute {

    private float estimatedTime;
    private float maxWeightOnCourse;
    private List<WayPoint> bestOrderOfDelivery;

    public OrderRoute() {
        // Default constructor without parameters.
    }

    public OrderRoute(float estimatedTime, float maxWeightOnCourse, List<WayPoint> bestOrderOfDelivery) {
        this.estimatedTime = estimatedTime;
        this.maxWeightOnCourse = maxWeightOnCourse;
        this.bestOrderOfDelivery = bestOrderOfDelivery;
    }

    public float getEstimatedTime() {
        return estimatedTime;
    }

    public float getMaxWeightOnCourse() {
        return maxWeightOnCourse;
    }

    public List<WayPoint> getBestOrderOfDelivery() {
        return bestOrderOfDelivery;
    }
}
