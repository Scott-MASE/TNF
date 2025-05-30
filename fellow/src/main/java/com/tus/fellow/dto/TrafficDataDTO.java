package com.tus.fellow.dto;

import java.time.LocalDateTime;



import lombok.AllArgsConstructor;
import lombok.Data;

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
