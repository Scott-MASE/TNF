package com.tus.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
}



