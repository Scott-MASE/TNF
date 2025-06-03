package com.tus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


public class AnomalyDTO {
    private Long id;
    private Integer nodeId;
    private Integer networkId;
    private String anomalyType;
    private Double trafficVolume;
    private LocalDateTime timestamp;

    public AnomalyDTO(Integer nodeId, Integer networkId, String anomalyType, Double trafficVolume, LocalDateTime timestamp) {
        this.nodeId = nodeId;
        this.networkId = networkId;
        this.anomalyType = anomalyType;
        this.trafficVolume = trafficVolume;
        this.timestamp = timestamp;
    }

    public AnomalyDTO() {
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

    public String getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(String anomalyType) {
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