package com.tus.uiandrest.controllers;


import com.tus.common.dto.TrafficDataDTO;
import com.tus.common.entity.TrafficData;
import com.tus.uiandrest.repositories.TrafficDataRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Kafka-Integrated REST Controllers
@RestController
@RequestMapping("/api/traffic")
@Tag(name = "Traffic Data", description = "Traffic Data API")
public class TrafficDataController {

	private TrafficDataRepository trafficRepo;
	
//	private TrafficDataProducer producer;

//	public TrafficDataController(TrafficDataRepository trafficRepo, TrafficDataProducer producer) {
//		this.trafficRepo = trafficRepo;
//		this.producer = producer;
//	}
	
	public TrafficDataController(TrafficDataRepository trafficRepo) {
		this.trafficRepo = trafficRepo;
	}
	
//	@Operation(summary = "Send traffic data (via Kafka)")
//	@ApiResponse(responseCode = "202", description = "Accepted")
//	@PostMapping
//	public ResponseEntity<Void> sendTraffic(@RequestBody TrafficDataDTO data) {
//		 System.out.println("MY DATA THROUGH POST^^^^^^^^^^^^^^:"+data.getNodeId()+"::"+data.getNetworkId()+"::"+data.getTrafficVolume());
//	   producer.sendTrafficData(data);
//	   return ResponseEntity.accepted().build();
//	}
	/**
	 * This is the POST method to send the traffic Data manually through the REST Swagger.
	 * @param data TrafficDataDTO (in the form of JSON object)
	 * @return ResponseEntity
	 */
	@PostMapping
	public ResponseEntity<Void> sendTraffic(@RequestBody TrafficDataDTO data) {
		TrafficData entity = new TrafficData();
		entity.setNodeId(data.getNodeId());
		entity.setNetworkId(data.getNetworkId());
		entity.setTrafficVolume(data.getTrafficVolume());
		entity.setTimestamp(data.getTimestamp());
		trafficRepo.save(entity);
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