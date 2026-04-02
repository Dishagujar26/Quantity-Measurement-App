package com.app.quantitymeasurementapp.service;

import com.app.quantitymeasurementapp.dto.QuantityDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;

import java.util.List;

/**
 * Service interface for all quantity measurement operations.
 *
 * UC17 changes from UC16:
 *  - Methods now return QuantityMeasurementDTO instead of raw values/booleans,
 *    giving callers a rich response that includes operation type, result, and
 *    error details.
 *  - New history/count retrieval methods added.
 */
public interface IQuantityMeasurementService {

    // ── Core operations ───────────────────────────────────────────────────

    QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO convert(QuantityDTO q, String targetUnit);

    QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2);

    // ── History / analytics ───────────────────────────────────────────────

    /** All operations of a given type (e.g. "ADD", "COMPARE"). */
    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    /** All operations for a given measurement type (e.g. "LengthUnit"). */
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    /** Count of successful operations of a given type. */
    long getOperationCount(String operation);

    /** All records that resulted in an error. */
    List<QuantityMeasurementDTO> getErrorHistory();
}
