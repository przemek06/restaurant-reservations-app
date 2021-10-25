package com.example.demo.service;

import com.example.demo.entity.TableEntity;
import com.example.demo.model.Table;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.TableRepository;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TableRepository tableRepository;

    public List<Table> findSuitableTables(int seats, String start, int duration) {
        Date date;
        try{
            date = ISODateTimeFormat
                    .dateTimeNoMillis()
                    .parseDateTime(start.replace(" ", "+")).toDate();
        } catch (Exception e){
            return new ArrayList<>();
        }
        List<TableEntity> tables = tableRepository.findSuitableTables(seats);
        List<Long> busyTableNumbers=reservationRepository.findBusyTables(date, duration);
        return tables.stream().filter(t->!busyTableNumbers.contains(t.getNumber())).map(Table::new).collect(Collectors.toList());
    }
}
