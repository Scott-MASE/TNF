package com.tus.fellow.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
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
	private String anomalyType;
	private Double trafficVolume;
	@Column(name = "datetime")
	private LocalDateTime timestamp;
}