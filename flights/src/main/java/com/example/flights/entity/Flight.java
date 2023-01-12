package com.example.flights.entity;

import com.example.flights.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NonNull
    private String orderNumber;
    @NonNull
    private String amount; //to be parsed -> BigDecimal
    @NonNull
    private OffsetDateTime startDate;
    @NonNull
    private OffsetDateTime endDate; //https://stackoverflow.com/questions/71941500/dealing-with-simpledateformat-passed-in-requestbody

    private Type type;
}
