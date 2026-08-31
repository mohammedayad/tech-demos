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
 * Represents a user belonging to a school.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String userId; // identifier for the user, we also could use email

    private String name;

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "school_id",
            nullable = false
    )
    private School school;
}
