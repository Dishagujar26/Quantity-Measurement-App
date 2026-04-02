package com.qma.history.controller;

import com.qma.history.dto.QuantityHistoryDTO;
import com.qma.history.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<QuantityHistoryDTO>> getHistoryByOperation(@PathVariable String operation) {
        return ResponseEntity.ok(historyService.getHistoryByOperation(operation));
    }

    @GetMapping("/type/{measurementType}")
    public ResponseEntity<List<QuantityHistoryDTO>> getHistoryByMeasurementType(@PathVariable String measurementType) {
        return ResponseEntity.ok(historyService.getHistoryByMeasurementType(measurementType));
    }

    @GetMapping("/count/{operation}")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        return ResponseEntity.ok(historyService.getOperationCount(operation));
    }

    @GetMapping("/errored")
    public ResponseEntity<List<QuantityHistoryDTO>> getErrorHistory() {
        return ResponseEntity.ok(historyService.getErrorHistory());
    }
}