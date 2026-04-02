package com.app.quantitymeasurementapp.repository;

import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for QuantityMeasurementEntity.
 *
 * Replaces the entire UC16 JDBC repository with zero SQL boilerplate.
 * Method names follow Spring Data naming conventions for auto-generated queries.
 */
@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    // ── Derived query methods (auto-generated from method names) ──────────

    /**
     * Find all records where operation matches the given value (case-sensitive).
     * SQL: SELECT * FROM ... WHERE operation = ?
     */
    List<QuantityMeasurementEntity> findByOperation(String operation);

    /**
     * Find all records for a specific measurement type.
     * SQL: SELECT * FROM ... WHERE this_measurement_type = ?
     */
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    /**
     * Find all records created after the given date.
     * SQL: SELECT * FROM ... WHERE created_at > ?
     */
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Count successful (non-error) records for a given operation.
     * SQL: SELECT COUNT(*) FROM ... WHERE operation = ? AND is_error = false
     */
    long countByOperationAndErrorFalse(String operation);

    /**
     * Find all error records.
     * SQL: SELECT * FROM ... WHERE is_error = true
     */
    List<QuantityMeasurementEntity> findByErrorTrue();

    // ── Custom @Query methods ─────────────────────────────────────────────

    /**
     * Find successful operations of a given type using JPQL.
     * Equivalent to: SELECT * FROM ... WHERE operation = :operation AND is_error = false
     */
    @Query("SELECT e FROM QuantityMeasurementEntity e " +
           "WHERE e.operation = :operation AND e.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(@Param("operation") String operation);

    /**
     * Find the most recent N operations.
     */
    @Query("SELECT e FROM QuantityMeasurementEntity e ORDER BY e.createdAt DESC")
    List<QuantityMeasurementEntity> findRecentOperations();

    /**
     * Count all operations grouped by operation type — native SQL example.
     */
    @Query(value = "SELECT operation, COUNT(*) as cnt FROM quantity_measurement_entity GROUP BY operation",
           nativeQuery = true)
    List<Object[]> countByOperationGrouped();
}
