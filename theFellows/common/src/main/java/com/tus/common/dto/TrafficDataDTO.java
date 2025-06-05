package com.tus.common.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//DTOs

@Data
@NoArgsConstructor
public class TrafficDataDTO {

 private Long id;
 private Integer nodeId;
 private Integer networkId;
 private Double trafficVolume;
 private LocalDateTime timestamp;

 public TrafficDataDTO(Integer nodeId, Integer networkId, Double trafficVolume, LocalDateTime timestamp) {
  this.nodeId = nodeId;
  this.networkId = networkId;
  this.trafficVolume = trafficVolume;
  this.timestamp = timestamp;
 }




}
