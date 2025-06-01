package com.tus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

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