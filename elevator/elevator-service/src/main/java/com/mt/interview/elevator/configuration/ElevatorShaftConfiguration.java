package com.mt.interview.elevator.configuration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "mt.elevator.shaft")
@Configuration
public class ElevatorShaftConfiguration {

    private FloorConfiguration      floors;
    private SimulationConfiguration simulation;

    @Data
    public static class FloorConfiguration {
        private Integer max;
    }

    @Data
    public static class SimulationConfiguration {

        @Positive
        private long delay = 1000;

        @Max(100)
        @Min(1)
        private Integer newPassengerOdds = 10;

    }

}
