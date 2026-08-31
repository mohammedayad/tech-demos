package com.ayad.applicationlibrary.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents an application that can be defined at the root,
 * organization, or school level.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String applicationId;

    private String name;

    private String url;

    @Enumerated(EnumType.STRING)
    private ApplicationLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant lastUpdate;
}
