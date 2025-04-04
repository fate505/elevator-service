package com.mt.interview.elevator.service;

import com.mt.interview.elevator.configuration.ElevatorShaftConfiguration;
import com.mt.interview.elevator.event.OpenDoorsEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Service
public class SimpleFloorService implements ApplicationListener<OpenDoorsEvent> {

    private final ElevatorShaftConfiguration elevatorShaftConfiguration;
    private final ShaftService               shaftService;
    private final Random                     random = new Random();

    /**
     * Helper method to return a random floor which is accessible in the shaft
     *
     * @return the floor number of the valid floor
     */
    public Integer getValidFloor() {
        return this.getRandom()
                   .nextInt(this.getElevatorShaftConfiguration()
                                .getFloors()
                                .getMax());
    }

    /**
     * Adds a new passenger with a random destination and source
     */
    public void addPassenger() {

        log.trace("Adding random passenger without floor");
        this.addPassenger(this.getValidFloor());

    }

    /**
     * Adds a new passenger with a specific source and a random destination
     *
     * @param floor the floor number which the passenger starts from
     */
    public void addPassenger(Integer floor) {

        log.debug("Adding random passenger on floor: {}", floor);
        this.addPassenger(floor, this.getValidFloor());

    }

    /**
     * Adds a passenger with a specified source and destination
     *
     * @param floor       the source floor number
     * @param destination the destination floor number
     */
    public void addPassenger(Integer floor, Integer destination) {

        log.debug("Adding passenger on floor: {} to floor: {}", floor, destination);
        if (floor.equals(destination)) {
            log.trace("Passenger cannot be added to the same floor");
            return;
        }

        this.getShaftService()
            .addDestination(floor);
        this.getShaftService()
            .getWaitingPassengers()
            .add(floor, destination);

    }

    /**
     * Removes the waiting passengers from a floor and adds them to the elevator
     *
     * @param floor the floor number being cleared of waiting passengers
     */
    public void clearPassengers(Integer floor) {

        log.debug("Clearing passengers on floor: {}", floor);
        final var destinations = this.getShaftService()
                                     .getWaitingPassengers()
                                     .get(floor);
        if (destinations == null) {
            return;
        }
        destinations.forEach(this.getShaftService()::addDestination);
        destinations.clear();


    }

    @Override
    public void onApplicationEvent(OpenDoorsEvent event) {

        log.trace("Doors opened on floor: {}", event.getFloor());
        this.clearPassengers(event.getFloor());

    }
}
