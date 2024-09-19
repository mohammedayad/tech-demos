package com.example.testservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONSUMER_BLOCK_EVENTS", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerBlockEvents implements Serializable {

  @EmbeddedId private ConsumerBlockEventsId id;
  private String blockReason;
  private LocalDateTime blockChangeDate;
  private LocalDateTime localBlockChangeDate;
  private LocalDateTime timeInserted;
}
