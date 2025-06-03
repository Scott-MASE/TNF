package com.tus.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "anomalies", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "nodeId", "networkId", "datetime" }) })
public class Anomaly {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer nodeId;
	
	private Integer networkId;

	@Enumerated(EnumType.STRING)
	private AnomalyType anomalyType;
	
	private Double trafficVolume;
	
	@Column(name = "datetime")
	private LocalDateTime timestamp;

	public Anomaly(Integer nodeId, Integer networkId, AnomalyType anomalyType, Double trafficVolume, LocalDateTime timestamp) {
		this.nodeId = nodeId;
		this.networkId = networkId;
		this.anomalyType = anomalyType;
		this.trafficVolume = trafficVolume;
		this.timestamp = timestamp;
	}


}