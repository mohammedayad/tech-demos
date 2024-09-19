package com.example.testservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEmailOptinEventsId implements Serializable {

  private String consumerId;
  private boolean emailOptIn;
  private long emailOptInChangeDateInMs;
}
