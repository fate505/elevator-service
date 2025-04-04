package com.mt.interview.elevator.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public abstract class DoorsEvent extends ApplicationEvent {

    final Integer floor;

    public DoorsEvent(Object source) {
        super(source);
        if (source instanceof Integer) {
            this.floor = (Integer) source;
        } else {
            throw new IllegalArgumentException("Source must be an Integer");
        }
        log.trace("Door Event Triggered");
    }

}
