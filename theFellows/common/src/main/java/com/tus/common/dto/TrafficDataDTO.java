package com.tus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

//DTOs
@Data
@AllArgsConstructor
public class TrafficDataDTO {
 public TrafficDataDTO() {
		// TODO Auto-generated constructor stub
	}
private Long id;
 private Integer nodeId;
 private Integer networkId;
 private Double trafficVolume;
 private LocalDateTime timestamp;
}
