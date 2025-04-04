package com.mt.interview.elevator.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public final class Coordinate {

    @Nullable
    private Direction direction;
    private Integer floor;

    public enum Direction {
        UP, DOWN;
    }
}
