package com.example.flights.entity;

import com.example.flights.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private BigInteger orderNumber;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Instant startDate;
    @NonNull
    private Instant endDate;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    public Flight(@NonNull BigInteger orderNumber, @NonNull BigDecimal amount, @NonNull Instant startDate, @NonNull Instant endDate, Type type) {
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }
}
