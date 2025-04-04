package com.mt.interview.elevator.service;

import com.mt.interview.elevator.dto.Elevator;
import org.springframework.util.MultiValueMap;

public interface ShaftService {

    /**
     * Adds a destination floor to the elevator route
     *
     * @param floor the destination floor number
     */
    void addDestination(Integer floor);

    /**
     * The collection of all waiting passengers for each floor.
     * The key is the floor number and the list is the requested destination floor number of the waiting passenger
     *
     * @return a multi value map of all waiting passenger information
     */
    MultiValueMap<Integer, Integer> getWaitingPassengers();

    /**
     * Action to advance the time by one tick
     */
    void tick();

    /**
     * Returns the current state of the elevator in the shaft
     *
     * @return the elevator state
     */
    Elevator.State getState();

}
