package com.example.flights.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    @NotBlank
    private String orderNumber;
    @NotBlank
    private String amount;
    @NotBlank
    private String startDate;
    @NotBlank
    private String endDate;
    @NotBlank
    private String type;
}
