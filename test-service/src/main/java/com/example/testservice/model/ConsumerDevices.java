package com.example.testservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONSUMER_DEVICES", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ConsumerDevices implements Serializable, Persistable<ConsumerDevicesId> {

  @EmbeddedId private ConsumerDevicesId id;
  private String deviceClientId;
  private String deviceStatus;
  private String deviceJailbreakState;
  private String deviceBetaType;
  private LocalDateTime deviceLastUsed;
  private LocalDateTime localDeviceLastUsed;
  private String deviceOrigin;
  private String deviceSeaId;
  private String deviceAdId;
  private Integer deviceSdkVersion;
  private Integer deviceAppVersion;
  private String deviceOsVersion;
  private String deviceSourceApplication;
  private String deviceName;
  private String deviceModel;
  @CreatedDate private LocalDateTime timeInserted;
  @LastModifiedDate private LocalDateTime lastUpdatedTime;

  @Override
  public boolean isNew() {
    return this.timeInserted == null;
  }
}
