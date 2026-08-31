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
 * Represents an organization that owns one or more schools.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant lastUpdate;

    @OneToMany(mappedBy = "organization",
            cascade = CascadeType.ALL)
    private List<School> schools = new ArrayList<>();
}
