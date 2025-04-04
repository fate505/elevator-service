package com.mt.interview.elevator.service;

import com.mt.interview.elevator.dto.Coordinate;
import com.mt.interview.elevator.dto.Elevator;
import com.mt.interview.elevator.event.OpenDoorsEvent;
import com.mt.interview.elevator.event.TickEvent;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A manager which contains a single elevator.
 * The elevator has no max capacity, no floor restrictions, no security restrictions, no emergency stop, no offline status.
 * <p>
 * All passengers are guaranteed to reach their destination.
 * The next action is selected by moving in the same direction to the closest matching coordinate.
 * A coordinate matches when the direction is null or matching the movement of the elevator.
 * <p>
 * When all else equal, elevators have a direction preference to UP.
 * The elevator is on floor 0, a call is made to -1 and 1. The elevator will go to 1.
 * <p>
 * Opening and closing doors cost 1 tick
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Getter(AccessLevel.PROTECTED)
public class SimpleShaftService implements ShaftService {

    @Getter
    private final MultiValueMap<Integer, Integer> waitingPassengers = new LinkedMultiValueMap<>();
    private final ApplicationEventPublisher       applicationEventPublisher;
    private final Elevator                        elevator          = Elevator.builder()
                                                                              .id(UUID.randomUUID())
                                                                              .state(Elevator.State.defaultState())
                                                                              .build();

    private Coordinate getCurrentLocation() {
        return this.getState()
                   .location();
    }

    private int getCurrentFloor() {
        return this.getCurrentLocation()
                   .floor();
    }

    private Set<Integer> getDestinations() {

        return this.getState()
                   .destinations();

    }

    private boolean isDoorsOpen() {
        return this.getState()
                   .status()
                   .getDoorsOpen();
    }

    @Override
    public void addDestination(Integer floor) {

        if (this.getDestinations()
                .add(floor)) {
            log.debug("Floor added: {}", floor);
        } else {
            log.debug("Floor already exists: {}", floor);
        }

    }

    protected Boolean checkChangeDoorState() {

        log.debug("FLOOR: {}", this.getCurrentFloor());
        log.debug("DOORS OPEN: {}", this.isDoorsOpen());
        log.debug("DESTINATIONS: {}", this.getDestinations());


        if (this.isDoorsOpen()
            && !this.getDestinations()
                    .isEmpty()
            //check waiting people
        ) {
            this.getState()
                .status()
                .setDoorsOpen(false);
            log.trace("Closing doors");
            this.getApplicationEventPublisher()
                .publishEvent(new OpenDoorsEvent(this.getCurrentFloor()));
            return true;

        } else if (this.getDestinations()
                       .remove(this.getCurrentFloor())) {
            this.getState()
                .status()
                .setDoorsOpen(true);
            log.trace("Opening doors");
            return true;
        }

        if (this.getDestinations().isEmpty() && !Optional.ofNullable(this.getWaitingPassengers()
                                                                         .get(this.getCurrentFloor()))
                                                         .orElse(List.of())
                                                         .isEmpty()) {
            this.getDestinations().add(this.getCurrentFloor());
        }

        return false;
    }

    @Override
    public void tick() {

        this.getApplicationEventPublisher()
            .publishEvent(new TickEvent(this.getCurrentFloor()));

        if (this.checkChangeDoorState()) {
            return;
        }
        final var nextDirection = this.getNextDirection();


        this.getCurrentLocation()
            .direction(nextDirection);

        if (nextDirection == Coordinate.Direction.UP) {
            this.getCurrentLocation()
                .floor(this.getCurrentFloor() + 1);
            log.trace("Moving up");
        } else if (nextDirection == Coordinate.Direction.DOWN) {
            this.getCurrentLocation()
                .floor(this.getCurrentFloor() - 1);
            log.trace("Moving down");
        }

    }

    @Override
    public Elevator.State getState() {
        return this.getElevator()
                   .state();
    }

    @Nullable
    protected Coordinate.Direction getNextDirection() {
        if (
                this.getDestinations()
                    .isEmpty()
        ) {
            log.trace("No destinations");
            return null;
        } else if (
                (
                        this.getCurrentLocation()
                            .direction() == null
                        || this.getCurrentLocation()
                               .direction()
                               .equals(Coordinate.Direction.UP)
                )
                && this.getDestinations()
                       .stream()
                       .anyMatch(destination -> destination > this.getCurrentFloor())
        ) {
            return Coordinate.Direction.UP;
        } else if (this.getDestinations()
                       .stream()
                       .anyMatch(destination -> destination < this.getCurrentFloor())) {
            return Coordinate.Direction.DOWN;
        } else {
            return null;
        }
    }


}
