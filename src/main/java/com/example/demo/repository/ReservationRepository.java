package com.example.demo.repository;

import com.example.demo.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

   @Query(nativeQuery = true,
   value = "select table_number from reservations where " +
           ":start between reservation_date and date_add(reservation_date, interval duration minute) " +
           "or date_add(:start, interval :duration minute) between reservation_date and date_add(reservation_date, interval duration minute) " +
           "or (:start<reservation_date and date_add(:start,interval duration minute) >reservation_date);")
    List<Long> findBusyTables(Date start, int duration);

    @Query(nativeQuery = true,
            value = "select * from reservations r where :date=date(reservation_date)")
    List<ReservationEntity> findAllByDate(Date date);
}
