package com.example.demo.repository;

import com.example.demo.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    @Query(nativeQuery = true,
            value = "select * from  tables t where :seats between t.min_number_of_seats and t.max_number_of_seats")
    List<TableEntity> findSuitableTables(int seats);

    Optional<TableEntity> getTableByNumber(Long number);
}
