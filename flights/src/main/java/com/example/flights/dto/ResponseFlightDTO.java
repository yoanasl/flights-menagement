package com.example.flights.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ResponseFlightDTO {
    @NonNull
    private Integer id;
    @NonNull
    private String orderNumber;
    @NonNull
    private String amount;
    @NonNull
    private String startDate;
    @NonNull
    private String endDate;
    @NonNull
    private String type;
}
