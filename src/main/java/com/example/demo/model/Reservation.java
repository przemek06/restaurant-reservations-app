package com.example.demo.model;


import com.example.demo.entity.ReservationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.format.ISODateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reservation {

    public Reservation(ReservationEntity entity) {
        this.date= ISODateTimeFormat.dateTimeNoMillis().print(entity.getDate().getTime());
        this.seatNumber=entity.getTable().getNumber();
        this.phone=entity.getPhone();
        this.email=entity.getEmail();
        this.fullName=entity.getFullName();
        this.numberOfSeats=entity.getNumberOfSeats();
        this.duration=entity.getDuration();
    }

    @NotNull
    private String date;
    @NotNull
    private Integer duration;
    @NotNull
    private Long seatNumber;
    @NotNull
    @Size(min=1, max=255)
    private String fullName;
    @NotNull
    @Size(min =9,max = 11)
    private String phone;
    @NotNull
    private Integer numberOfSeats;
    @Email
    @Size(min=1,max=255)
    private String email;

    public String printNicely(){
        return "Reservation for " +getFullName()+ "\n"+
                "date: "+getDate() +"\n"+
                "table number: "+getSeatNumber()+ "\n"+
                "number of seats: "+getNumberOfSeats();
    }
}
