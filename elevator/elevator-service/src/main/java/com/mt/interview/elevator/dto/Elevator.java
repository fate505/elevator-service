package com.mt.interview.elevator.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
public record Elevator(
        UUID id,
        State state
) {

    @Builder
    public record State(
            Status status,
            Coordinate location,
            Set<Integer> destinations
    ) {

        public static State defaultState() {
            return Elevator.State.builder()
                                 .location(Coordinate.builder()
                                                     .floor(0)
                                                     .build())
                                 .destinations(new HashSet<>())
                                 .status(Elevator.State.Status.defaultStatus())
                                 .build();
        }

        @Data
        @Builder
        public static class Status {
            Boolean online;
            Boolean doorsOpen;

            public static Status defaultStatus() {
                return Status.builder()
                             .online(true)
                             .doorsOpen(true)
                             .build();
            }
        }

    }


}
