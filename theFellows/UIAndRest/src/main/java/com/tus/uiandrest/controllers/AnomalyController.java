package com.tus.uiandrest.controllers;


import com.tus.common.entity.Anomaly;
import com.tus.uiandrest.repositories.AnomalyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@Tag(name = "Anomalies", description = "Anomaly API")
public class AnomalyController {

    @Autowired
    private AnomalyRepository anomalyRepo;

    public AnomalyController(AnomalyRepository anomalyRepo){
        this.anomalyRepo = anomalyRepo;
    }

    @Operation(summary = "Get all anomalies")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public List<Anomaly> getAllAnomalies() {
        return anomalyRepo.findAll();
    }

    @Operation(summary = "Get anomaly by ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @GetMapping("/{id}")
    public ResponseEntity<Anomaly> getAnomalyById(@PathVariable Long id) {
        return anomalyRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get anomalies by Network ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/network/{networkId}")
    public List<Anomaly> getByNetworkId(@PathVariable Integer networkId) {
        return anomalyRepo.findByNetworkId(networkId);
    }
}

