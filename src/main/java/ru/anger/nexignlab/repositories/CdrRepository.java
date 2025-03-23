package ru.anger.nexignlab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.anger.nexignlab.models.Cdr;

import java.time.LocalDateTime;
import java.util.List;

public interface CdrRepository extends JpaRepository<Cdr, Integer>{
    @Override
    void deleteAll();

    @Query(value="SELECT * FROM CDR WHERE CALLER = :msisdn OR RECEIVER = :msisdn", nativeQuery = true)
    List<Cdr> findByCallerOrReceiver(String msisdn);

    @Query(value="SELECT * FROM CDR WHERE (CALLER = :msisdn OR RECEIVER = :msisdn) AND START_TIME BETWEEN :startTime AND :endTime", nativeQuery = true)
    List<Cdr> findByCallerOrReceiver(String msisdn, LocalDateTime startTime, LocalDateTime endTime);

    @Query(value="SELECT DISTINCT CALLER FROM CDR", nativeQuery = true)
    List<String> findAllSubscribers();

    @Query(value="SELECT DISTINCT CALLER FROM CDR WHERE START_TIME BETWEEN :startTime AND :endTime", nativeQuery = true)
    List<String> findAllSubscribers(LocalDateTime startTime, LocalDateTime endTime);
}

