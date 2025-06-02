package com.tus.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "anomalies")
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

    public Long getId() {
        return id;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public String getAnomalyType() {
        return anomalyType;
    }

    public Double getTrafficVolume() {
        return trafficVolume;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    public void setAnomalyType(String anomalyType) {
        this.anomalyType = anomalyType;
    }

    public void setTrafficVolume(Double trafficVolume) {
        this.trafficVolume = trafficVolume;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}