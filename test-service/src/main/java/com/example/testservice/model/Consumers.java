package com.example.testservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONSUMERS", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Consumers implements Serializable, Persistable<String> {

  @Id
  private String consumerId;
  private String firstName;
  private String lastName;
  private String phone;
  private Boolean phoneConfirmed;
  private String email;
  private Boolean emailConfirmed;
  private String addressStreet;
  private String addressNumber;
  private String addressPostalCode;
  private String addressCity;
  private String addressCountry;
  private String languageTag;
  private String nationality;
  private String bankAccountIban;
  private String bankAccountTransferType;
  private String bankAccountSource;
  private String bankAccountBic;
  private String bankAccountMandateReference;
  private LocalDateTime bankAccountMandateSignDate;
  private LocalDateTime localBankAccountMandateSignDate;
  private String bankAccountAccountHolderName;
  private String bankAccountStatus;
  private Boolean unregistered;
  private LocalDateTime unregisteredDate;
  private LocalDateTime localUnregisteredDate;
  private String consumerType;
  private Boolean emailOptIn;
  private long emailOptInChangeDateInMs;
  private Boolean adminBlocked;
  private long adminBlockChangeDateInMs;
  private LocalDateTime onboardingStartDate;
  private LocalDateTime localOnboardingStartDate;
  private LocalDateTime onboardingEndDate;
  private LocalDateTime localOnboardingEndDate;
  private LocalDate dateOfBirth;
  private String placeOfBirth;
  private String verifiedUserInfoFirstNames;
  private String verifiedUserInfoLastName;
  private LocalDateTime verifiedUserInfoExpirationDate;
  private LocalDateTime localVerifiedUserInfoExpirationDate;
  private LocalDateTime verifiedUserInfoVerificationDate;
  private LocalDateTime localVerifiedUserInfoVerificationDate;
  private String verifiedUserInfoVerifyingParty;
  private Boolean kycBlocked;
  private long kycBlockChangeDateInMs;
  private Boolean acceptedByCompliance;
  private Boolean transactionPushNotificationEnabled;
  @CreatedDate private LocalDateTime timeInserted;
  @LastModifiedDate private LocalDateTime lastUpdatedTime;

  @Override
  public String getId() {
    return this.consumerId;
  }

  @Override
  public boolean isNew() {
    return this.timeInserted == null;
  }
}
