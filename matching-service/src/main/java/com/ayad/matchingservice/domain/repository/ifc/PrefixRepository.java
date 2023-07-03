package com.ayad.matchingservice.domain.repository.ifc;

import com.ayad.matchingservice.domain.model.PrefixEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for managing prefix entities.
 */
@Repository
public interface PrefixRepository extends JpaRepository<PrefixEntity, Long> {

    /**
     * Finds all {@link PrefixEntity} instances whose prefix is a prefix of the given input string,
     * sorted by descending order of prefix length.
     *
     * @param inputString the input string to match against the prefix of the {@link PrefixEntity} instances.
     * @return an {@link Optional} object containing a {@link List} of {@link PrefixEntity} instances whose prefix is
     * a prefix of the given input string, sorted by descending order of prefix length, or an empty {@link Optional}
     * object if no matching {@link PrefixEntity} instances are found.
     */
    @Query("SELECT p FROM PrefixEntity p WHERE :inputString LIKE CONCAT(p.prefix, '%') ORDER BY LENGTH(p.prefix) DESC")
    Optional<List<PrefixEntity>> findByMatchingPrefix(@Param("inputString") String inputString);
}
