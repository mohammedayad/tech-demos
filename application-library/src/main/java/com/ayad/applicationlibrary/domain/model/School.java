package com.ayad.applicationlibrary.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;


/**
 * Represents a school that belongs to an organization
 * and contains one or more users.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class School {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;
    @OneToMany(mappedBy = "school",
            cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
}
