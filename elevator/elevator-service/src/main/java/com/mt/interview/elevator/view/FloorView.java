package com.mt.interview.elevator.view;

import com.mt.interview.elevator.dto.Elevator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter(AccessLevel.PROTECTED)
public class FloorView extends HorizontalLayout {

    private final Integer floorNumber;
    private final Button floorButton;
    private final Span waitingPassengers = new Span();


    public FloorView(Integer floorNumber) {
        this.floorNumber = floorNumber;
        this.floorButton = new Button(floorNumber.toString(), VaadinIcon.STOP.create());
        this.floorButton.setIconAfterText(true);
        this.add(floorButton);
        this.add(waitingPassengers);
    }

    /**
     * Updates the visual representation of a floor's state.
     * </p>
     * When a floor is marked as a destination the button is disabled
     * The icon in the button represents the state of the elevator:
     * OPEN_BOOK -> the doors are open and the elevator is on the floor
     * BOOK -> the doors are closed and the elevator is on the floor
     * STOP -> the elevator is not on that floor
     * </p>
     * The waiting passengers are listed beside the floor, each represented by their desired destination
     *
     * @param waitingPassengers the waiting passenger data
     * @param state the state of the elevator
     */
    public void setState(MultiValueMap<Integer, Integer> waitingPassengers, Elevator.State state) {

        this.getFloorButton()
                .setEnabled(!state.destinations().contains(this.getFloorNumber()));
        this.getFloorButton()
                .setIcon(VaadinIcon.STOP.create());
        if (state.location()
                .floor()
                .equals(this.getFloorNumber())) {
            if (state.status().getDoorsOpen()) {
                this.getFloorButton()
                        .setIcon(VaadinIcon.OPEN_BOOK.create());
            } else {
                this.getFloorButton()
                        .setIcon(VaadinIcon.BOOK.create());
            }
        }
        this.getWaitingPassengers()
                .setText(
                        Optional.ofNullable(waitingPassengers.get(this.getFloorNumber()))
                                .stream()
                                .flatMap(Collection::stream)
                                .map(Object::toString)
                                .collect(Collectors.joining(", "))
                );

    }

}
