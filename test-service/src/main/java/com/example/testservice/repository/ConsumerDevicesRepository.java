package com.example.testservice.repository;

import com.example.testservice.model.ConsumerDevices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerDevicesRepository extends JpaRepository<ConsumerDevices, String> {

  @Modifying
  @Query("delete from ConsumerDevices cd where cd.id.consumerId in :consumerIds")
  void deleteAllByConsumerId(Iterable<String> consumerIds);
}
