package com.example.demo.model;


import com.example.demo.entity.TableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {

    public Table(TableEntity entity) {
        this.number=entity.getNumber();
        this.maxNumberOfSeats=entity.getMaxNumberOfSeats();
        this.minNumberOfSeats=entity.getMinNumberOfSeats();
    }

    private Long number;
    private Integer maxNumberOfSeats;
    private Integer minNumberOfSeats;

}
