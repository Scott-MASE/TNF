package com.tus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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




}