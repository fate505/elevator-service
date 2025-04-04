package com.mt.interview.elevator.service;

import com.mt.interview.elevator.configuration.ElevatorShaftConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Component
public class SimulationService {

    final Random                     random = new Random();
    final ElevatorShaftConfiguration elevatorShaftConfiguration;
    final SimpleFloorService         floorService;
    final ShaftService               shaftService;

    /**
     * The schedule to advance the simulation.
     * mt.elevator.shaft.simulation.delay -> sets the delay of each tick (1000ms default)
     * mt.elevator.shaft.simulation.new-passenger-odds -> for each tick the chances of creating a new passenger in the simulation (10% default)
     */
    @Scheduled(fixedDelayString = "${mt.elevator.shaft.simulation.delay}")
    public void scheduleFixedDelayTask() {

        final var i = this.getRandom()
                          .nextInt(100);
        log.debug("Adding possible passenger: {}", i);

        if (i < this.getElevatorShaftConfiguration()
                    .getSimulation()
                    .getNewPassengerOdds()) {
            this.getFloorService()
                .addPassenger();
        }

        this.getShaftService()
            .tick();
    }

}
