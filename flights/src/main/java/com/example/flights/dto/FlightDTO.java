package com.example.flights.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    @NonNull
    private String orderNumber;
    @NonNull
    private String amount;
    @NonNull
    private String startDate;
    @NonNull
    private String endDate;

    private String type;
}
