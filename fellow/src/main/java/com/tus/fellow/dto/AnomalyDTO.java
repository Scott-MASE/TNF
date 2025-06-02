package com.tus.fellow.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnomalyDTO {
    private Long id;
    private Integer nodeId;
    private Integer networkId;
    private String anomalyType;
    private Double trafficVolume;
    private LocalDateTime timestamp;
}