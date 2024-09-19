package com.example.testservice.repository;

import com.example.testservice.model.ConsumerEmailOptinEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerEmailOptinEventsRepository
    extends JpaRepository<ConsumerEmailOptinEvents, String> {

    @Modifying
    @Query("delete from ConsumerEmailOptinEvents ceoe where ceoe.id.consumerId=:consumerId")
    void deleteConsumerEmailOptinEventsByConsumerId(@Param("consumerId") String consumerId);
}
