package com.example.flights.dto;

import com.example.flights.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFlightDTO {
    @NonNull
    private String orderNumber;
    @NonNull
    private String amount;
    @NonNull
    private OffsetDateTime startDate;
    @NonNull
    private OffsetDateTime endDate;

    private Type type;
}
