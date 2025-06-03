package com.tus.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "traffic_data")
public class TrafficData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nodeId;
    private Integer networkId;
    private Double trafficVolume;

    @Column(name = "datetime")
    private LocalDateTime timestamp;

    public TrafficData(Integer nodeId, Integer networkId, Double trafficVolume, LocalDateTime timestamp) {
        this.nodeId = nodeId;
        this.networkId = networkId;
        this.trafficVolume = trafficVolume;
        this.timestamp = timestamp;
    }

    public TrafficData() {
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

