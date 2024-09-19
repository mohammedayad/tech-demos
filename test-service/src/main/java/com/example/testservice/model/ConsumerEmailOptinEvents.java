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
@Table(name = "CONSUMER_EMAIL_OPTIN_EVENTS", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEmailOptinEvents implements Serializable {

  @EmbeddedId private ConsumerEmailOptinEventsId id;
  private LocalDateTime emailOptInChangeDate;
  private LocalDateTime localEmailOptInChangeDate;
  private LocalDateTime timeInserted;
}
