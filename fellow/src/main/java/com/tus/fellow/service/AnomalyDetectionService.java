package com.tus.fellow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.fellow.entity.Anomaly;
import com.tus.fellow.entity.TrafficData;
import com.tus.fellow.repository.AnomalyRepository;

//Anomaly Detection Service
@Service
public class AnomalyDetectionService {

 @Autowired
 private AnomalyRepository anomalyRepository;

 public void checkForAnomaly(TrafficData data) {
     if (data.getTrafficVolume() > 1000) { // Example threshold rule
         Anomaly anomaly = new Anomaly();
         anomaly.setNodeId(data.getNodeId());
         anomaly.setNetworkId(data.getNetworkId());
         anomaly.setTrafficVolume(data.getTrafficVolume());
         anomaly.setAnomalyType("High Traffic Volume");
         anomaly.setTimestamp(data.getTimestamp());
         anomalyRepository.save(anomaly);
     }
 }
}