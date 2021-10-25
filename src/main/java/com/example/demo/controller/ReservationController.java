package com.example.demo.controller;

import com.example.demo.entity.ReservationEntity;
import com.example.demo.model.CancellationCode;
import com.example.demo.model.Reservation;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations(@RequestParam String date) {
        return reservationService.getReservations(date);
    }

    @PostMapping("/reservations")
    public ResponseEntity<String> makeReservation(@Valid @RequestBody Reservation reservation){
        return reservationService.makeReservation(reservation);
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<String> requestCancellation(@PathVariable Long id){
        return reservationService.requestCancellation(id);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> confirmCancellation(@PathVariable Long id,
                                                      @Valid @RequestBody CancellationCode code){
        return reservationService.confirmCancellation(id, code);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolation(ConstraintViolationException ex){
        return ResponseEntity.badRequest().body(ex.getConstraintViolations().stream().findFirst().get().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> argumentNotValid(){
        return ResponseEntity.badRequest().body("Bad parameter");
    }
}
