package com.ayad.matchingservice.domain.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Represents a prefix entity stored in the database.
 */

@Entity
@Table(name = "prefixes_matching")
@Getter
@Setter
@ToString
public class PrefixEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefix", nullable = false, unique = true)
    private String prefix;
}
