package com.tus.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


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

	public Anomaly() {
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public AnomalyType getAnomalyType() {
		return anomalyType;
	}

	public void setAnomalyType(AnomalyType anomalyType) {
		this.anomalyType = anomalyType;
	}

	public Double getTrafficVolume() {
		return trafficVolume;
	}

	public void setTrafficVolume(Double trafficVolume) {
		this.trafficVolume = trafficVolume;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}