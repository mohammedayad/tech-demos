package com.example.testservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerBasicDetails implements Serializable {

  private String consumerId;
  private Boolean adminBlocked;
  private long adminBlockChangeDateInMs;
  private Boolean kycBlocked;
  private long kycBlockChangeDateInMs;
  private Boolean emailOptIn;
  private long emailOptInChangeDateInMs;
  private LocalDateTime timeInserted;
}
