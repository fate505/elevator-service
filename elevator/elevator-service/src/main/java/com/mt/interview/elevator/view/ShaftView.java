package com.mt.interview.elevator.view;

import com.mt.interview.elevator.dto.Elevator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Getter(AccessLevel.PROTECTED)
public class ShaftView extends VerticalLayout {

    private final List<FloorView> floorViews = new ArrayList<>();
    private final Set<Integer> destinations = new HashSet<>();
    private final UI ui;

    public ShaftView(Integer maxFloors) {
        this(maxFloors, Set.of());
    }

    public ShaftView(Integer maxFloors, Set<Integer> destinations) {
        IntStream.range(0, maxFloors)
                .map(i -> maxFloors - i - 1)
                .mapToObj(FloorView::new)
                .peek(this.getFloorViews()::add)
                .forEach(this::add);
        this.destinations.addAll(destinations);
        this.ui = UI.getCurrent();
    }

    /**
     * Updates all the visual representations of the floors in the shaft
     *
     * @param waitingPassengers the data containing all the waiting passengers
     * @param state             the state of the elevator
     */
    public void updateFloors(MultiValueMap<Integer, Integer> waitingPassengers, Elevator.State state) {
        this.destinations.clear();
        this.floorViews.forEach(floorView -> this.updateFloor(floorView, waitingPassengers, state));
        this.destinations.addAll(state.destinations());
    }

    private void updateFloor(FloorView floorView,
                             MultiValueMap<Integer, Integer> waitingPassengers,
                             Elevator.State state) {
        this.getUI().ifPresent(
                ui -> ui.access(() -> {
                            floorView.setState(waitingPassengers, state);
                            ui.push();
                        }
                )
        );


    }

}
