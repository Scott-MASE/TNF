package com.tus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

//DTOs


public class TrafficDataDTO {
 public TrafficDataDTO() {
		// TODO Auto-generated constructor stub
	}

 public TrafficDataDTO(Integer nodeId, Integer networkId, Double trafficVolume, LocalDateTime timestamp) {
  this.nodeId = nodeId;
  this.networkId = networkId;
  this.trafficVolume = trafficVolume;
  this.timestamp = timestamp;
 }

 private Long id;
 private Integer nodeId;
 private Integer networkId;
 private Double trafficVolume;
 private LocalDateTime timestamp;

 public int getNodeId() {
  return nodeId;
 }

 public int getNetworkId() {
  return networkId;
 }

 public double getTrafficVolume() {
  return trafficVolume;
 }

 public LocalDateTime getTimestamp() {
  return timestamp;
 }

 public void setNodeId(Integer nodeId) {
  this.nodeId = nodeId;
 }

 public void setNetworkId(Integer networkId) {
  this.networkId = networkId;
 }

 public void setTrafficVolume(Double trafficVolume) {
  this.trafficVolume = trafficVolume;
 }

 public void setTimestamp(LocalDateTime timestamp) {
  this.timestamp = timestamp;
 }
}
