package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@javax.persistence.Table(name = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableEntity {
    @Id
    @Column(name = "number")
    private Long number;
    @Column(name = "minNumberOfSeats")
    private int minNumberOfSeats;
    @Column(name = "maxNumberOfSeats")
    private int maxNumberOfSeats;
}
