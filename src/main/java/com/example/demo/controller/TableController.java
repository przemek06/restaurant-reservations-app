package com.example.demo.controller;

import com.example.demo.model.Table;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TableController {

    @Autowired
    TableService tableService;

    @GetMapping("/tables")
    public ResponseEntity<List<Table>> findSuitableTables(@RequestParam int min_seats,
                                                          @RequestParam String start_date,
                                                          @RequestParam int duration) {
        return ResponseEntity.ok(tableService.findSuitableTables(min_seats, start_date, duration));
    }
}
