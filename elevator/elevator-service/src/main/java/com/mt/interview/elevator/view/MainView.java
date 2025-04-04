package com.mt.interview.elevator.view;

import com.mt.interview.elevator.configuration.ElevatorShaftConfiguration;
import com.mt.interview.elevator.event.TickEvent;
import com.mt.interview.elevator.service.ShaftService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@Getter(AccessLevel.PROTECTED)
@Route
@SpringComponent
public class MainView extends HorizontalLayout {

    private final ShaftService shaftService;
    private final ShaftView    shaftView;

    public MainView(
            ElevatorShaftConfiguration elevatorShaftConfiguration,
            ShaftService shaftService
                   ) {
        this.shaftService = shaftService;
        this.shaftView    = new ShaftView(elevatorShaftConfiguration.getFloors().getMax());
        this.add(this.shaftView);
    }


    @EventListener
    public void onTickEvent(TickEvent ignored) {
        this.getShaftView()
            .updateFloors(this.getShaftService().getWaitingPassengers(), this.getShaftService().getState());
    }
}
