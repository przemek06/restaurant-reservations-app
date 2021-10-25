package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservation_id")
    private Long reservation_id;
    @Column(name = "reservation_date")
    private Date date;
    private Integer duration;
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "table_number",
            referencedColumnName = "number"
    )
    private TableEntity table;
    private String fullName;
    private String phone;
    private Integer numberOfSeats;
    private String email;

}
