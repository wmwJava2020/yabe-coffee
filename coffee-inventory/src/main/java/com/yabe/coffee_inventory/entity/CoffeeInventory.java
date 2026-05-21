package com.yabe.coffee_inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "tbl_ivt")
@Data
public class CoffeeInventory {

    @Id
    private Long id = System.currentTimeMillis();
    private String coffeeName;
    private String coffeeType;
    private int quantity;
    private double price;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invDate = LocalDate.now();




}
