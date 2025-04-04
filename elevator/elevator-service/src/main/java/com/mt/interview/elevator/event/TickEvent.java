package com.mt.interview.elevator.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TickEvent extends ApplicationEvent {

    private final Integer currentFloor;
    public TickEvent(Object source) {
        super(source);
        if (!(source instanceof Integer)) {
            throw new IllegalArgumentException("Source must be an integer");
        }
        this.currentFloor = (Integer)source;
    }

}
