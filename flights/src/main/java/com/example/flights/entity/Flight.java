package com.example.flights.entity;

import com.example.flights.enums.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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
public class Flight{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private Long orderNumber;
    @NonNull
    @Positive
    private BigDecimal amount;
    @NonNull
    private Instant startDate;
    @NonNull
    private Instant endDate;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    public Flight(@NonNull Long orderNumber, @NonNull BigDecimal amount, @NonNull Instant startDate, @NonNull Instant endDate, Type type){
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public void setOrderNumber(Long orderNumber){
        if(orderNumber <= 0){
            throw new NumberFormatException("Amount cannot be negative!");
        }
        this.orderNumber = orderNumber;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
}
