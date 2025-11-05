package com.ahold.technl.sandbox.domain.repository.ifc;

import com.ahold.technl.sandbox.domain.model.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


/**
 * Repository interface for managing {@link Delivery} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and query methods.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    /**
     * Finds all deliveries that started between the specified timestamps, ordered by start time in ascending order.
     *
     * @param from the start timestamp
     * @param to   the end timestamp
     * @return a list of deliveries that started within the specified time range
     */
    // 1. Derived Query Methods (Method Name Query)
    List<Delivery> findAllByStartedAtBetweenOrderByStartedAtAsc(Instant from, Instant to);


    // find total count of deliveries in specific period
    // find avg time between deliveries (get current delivery started date - previous one)


    @Query(value =
            "Select count(startedTime) as totalCounts, COALESCE(avg(timeDiff),0) as avgDeliveriesTime from (select STARTED_AT as startedTime, TIMESTAMPDIFF(MINUTE, STARTED_AT, LEAD(STARTED_AT) over (order by STARTED_AT)) AS timeDiff  from deliveries where STARTED_AT between :from and :to)",
            nativeQuery = true)
    Object[] findYestardaySummaryDB(@Param("from")Instant from, @Param("to") Instant to);


   // 2. JPQL (Java Persistence Query Language)
//   @Query("SELECT d FROM Delivery d WHERE d.startedAt BETWEEN :from AND :to ORDER BY d.startedAt ASC")
//   List<Delivery> findDeliveriesBetween(@Param("from") Instant from, @Param("to") Instant to);

//    positional parameters like ?1 and ?2 in your JPQL query
//    @Query("SELECT d FROM Delivery d WHERE d.startedAt BETWEEN ?1 AND ?2 ORDER BY d.startedAt ASC")
//    List<Delivery> findDeliveriesBetween(Instant from, Instant to);


   // 3. Native SQL Queries
//    @Query(value = "SELECT * FROM delivery WHERE started_at BETWEEN :from AND :to ORDER BY started_at ASC", nativeQuery = true)
//    List<Delivery> findDeliveriesBetweenNative(@Param("from") Instant from, @Param("to") Instant to);


    // 4. Criteria API - Implemented in a custom repository implementation class (not shown here)
//    public List<Delivery> findDeliveriesBetween(Instant from, Instant to) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Delivery> query = cb.createQuery(Delivery.class);
//        Root<Delivery> root = query.from(Delivery.class);
//        query.select(root)
//                .where(cb.between(root.get("startedAt"), from, to))
//                .orderBy(cb.asc(root.get("startedAt")));
//        return entityManager.createQuery(query).getResultList();
//    }

    // 5. Named Queries (Defined in the Delivery entity class with @NamedQuery annotation
//    @Entity
//    @NamedQuery(
//            name = "Delivery.findDeliveriesBetween",
//            query = "SELECT d FROM Delivery d WHERE d.startedAt BETWEEN :from AND :to ORDER BY d.startedAt ASC"
//    )
//    public class Delivery {
//        // Entity fields and methods
//    }

    // Usage:
//    @Query(name = "Delivery.findDeliveriesBetween")
//    List<Delivery> findDeliveriesBetween2(@Param("from") Instant from, @Param("to") Instant to);

}
