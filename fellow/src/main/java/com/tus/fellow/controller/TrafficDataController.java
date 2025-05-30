package com.tus.fellow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.fellow.dto.TrafficDataDTO;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.kafka.TrafficDataProducer;
import com.tus.fellow.repository.TrafficDataRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

//Kafka-Integrated REST Controllers
@RestController
@RequestMapping("/api/traffic")
@Tag(name = "Traffic Data", description = "Traffic Data API")
public class TrafficDataController {

 @Autowired
 private TrafficDataRepository trafficRepo;

 @Autowired
 private TrafficDataProducer producer;

 @Operation(summary = "Send traffic data (via Kafka)")
 @ApiResponse(responseCode = "202", description = "Accepted")
 @PostMapping
 public ResponseEntity<Void> sendTraffic(@RequestBody TrafficDataDTO data) {
     producer.sendTrafficData(data);
     return ResponseEntity.accepted().build();
 }

 @Operation(summary = "Get all traffic data")
 @ApiResponse(responseCode = "200", description = "OK")
 @GetMapping
 public List<TrafficData> getAll() {
     return trafficRepo.findAll();
 }

 @Operation(summary = "Get traffic data by ID")
 @ApiResponse(responseCode = "200", description = "OK")
 @ApiResponse(responseCode = "404", description = "Not Found")
 @GetMapping("/{id}")
 public ResponseEntity<TrafficData> getById(@PathVariable Long id) {
     return trafficRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
 }

 @Operation(summary = "Get traffic data by Node ID")
 @ApiResponse(responseCode = "200", description = "OK")
 @GetMapping("/node/{nodeId}")
 public List<TrafficData> getByNodeId(@PathVariable Integer nodeId) {
     return trafficRepo.findByNodeId(nodeId);
 }
}