package com.example.demo.service;

import com.example.demo.entity.ReservationEntity;
import com.example.demo.entity.TableEntity;
import com.example.demo.model.CancellationCode;
import com.example.demo.model.Reservation;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.TableRepository;
import net.bytebuddy.utility.RandomString;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    ReservationRepository reservationRepository;
    TableRepository tableRepository;
    EmailService emailService;
    private Map<Long, CancellationCode> cancellationCodes;

    public ReservationService(ReservationRepository reservationRepository,
                              TableRepository tableRepository,
                              EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.emailService = emailService;
        this.cancellationCodes = new HashMap<>();
    }

    public ResponseEntity<List<ReservationEntity>> getReservations(String date) {
        Date formatted;
        try {
            formatted = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        return ResponseEntity.ok(reservationRepository.findAllByDate(formatted));
    }

    public ResponseEntity<String> makeReservation(Reservation reservation) {
        Date date;
        try {
            date = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(reservation.getDate()).toDate();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Date in wrong format");
        }
        List<Long> busyTables = reservationRepository.findBusyTables(date, reservation.getDuration());
        TableEntity table;
        Optional<TableEntity> optional = tableRepository.getTableByNumber(reservation.getSeatNumber());
        if (optional.isEmpty()) return ResponseEntity.badRequest().body("This table doesn't exist");
        table = optional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (reservation.getEmail() == null && authentication != null) {
            reservation.setEmail(((UserDetailsImpl) authentication.getPrincipal()).getEmail());
        }

        if (reservation.getEmail() == null) {
            return ResponseEntity.badRequest().body("You need to include your email or log in");
        }

        if (reservation.getNumberOfSeats() > table.getMaxNumberOfSeats() ||
                reservation.getNumberOfSeats() < table.getMinNumberOfSeats()) {
            return ResponseEntity.badRequest().body("Wrong number of seats at this table");
        }

        if (busyTables.contains(reservation.getSeatNumber())) {
            return ResponseEntity.badRequest().body("This table is already reserved");
        }

        if (date.before(Date.from(Instant.now()))) {
            return ResponseEntity.badRequest().body("You need to choose later date");
        }

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .date(date)
                .duration(reservation.getDuration())
                .email(reservation.getEmail())
                .fullName(reservation.getFullName())
                .numberOfSeats(reservation.getNumberOfSeats())
                .phone(reservation.getPhone())
                .table(table)
                .build();

        String subject = "Reservation";
        String content = "Your reservation: " + reservation.printNicely();
        emailService.sendSimpleMessage(reservation.getEmail(), subject, content);

        Long id = reservationRepository.save(reservationEntity).getReservation_id();
        return ResponseEntity.ok(id.toString());

    }

    public ResponseEntity<String> requestCancellation(Long id) {
        Optional<ReservationEntity> optional = reservationRepository.findById(id);
        if (optional.isPresent()) {
            ReservationEntity reservation = optional.get();
            if (Date.from(Instant.now().plusMillis(2 * 1000 * 60 * 60)).before(reservation.getDate())) {
                String random = RandomString.make();
                cancellationCodes.put(id, new CancellationCode(random, Date.from(Instant.now())));
                String subject = "Cancellation";
                String content = "Cancellation code: " + random + " for reservation at: " + reservation.getDate();
                emailService.sendSimpleMessage(reservation.getEmail(), subject, content);

                return ResponseEntity.ok("Requested cancellation");
            }
            return ResponseEntity.badRequest().body("Too late to cancel this reservation");
        }
        return ResponseEntity.badRequest().body("No such reservation exists");
    }

    public ResponseEntity<String> confirmCancellation(Long id, CancellationCode code) {
        if (cancellationCodes.get(id) != null) {
            if (cancellationCodes.get(id).getVerificationCode().equals(code.getVerificationCode())) {
                ReservationEntity reservation = reservationRepository.getById(id);
                reservationRepository.deleteById(id);
                cancellationCodes.remove(id);
                String subject = "Reservation cancelled";
                String content = "Reservation at " + reservation.getDate() + " cancelled.";
                emailService.sendSimpleMessage(reservation.getEmail(), subject, content);
                return ResponseEntity.ok("Reservation cancelled");
            }
            return ResponseEntity.badRequest().body("Wrong or expired code");
        }
        return ResponseEntity.badRequest().body("No such reservation");
    }

    @Scheduled(fixedRate = 1000 * 30)
    private void cleanReservations() {
        cancellationCodes =
                cancellationCodes.entrySet()
                        .stream()
                        .filter(p -> p.getValue().getCreation().after(Date.from(Instant.now().minusMillis(1000 * 60 * 5))))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
