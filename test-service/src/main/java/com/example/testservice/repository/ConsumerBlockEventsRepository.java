package com.example.testservice.repository;

import com.example.testservice.model.ConsumerBlockEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerBlockEventsRepository extends JpaRepository<ConsumerBlockEvents, String> {
    @Modifying
    @Query("delete from ConsumerBlockEvents cbe where cbe.id.consumerId=:consumerId")
    void deleteConsumerBlockEventsById(@Param("consumerId") String consumerId);
}
