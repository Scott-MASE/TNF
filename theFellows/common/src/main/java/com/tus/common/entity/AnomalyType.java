package com.tus.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public enum AnomalyType {
    HIGH_TRAFFIC_VOLUME,
    ZERO_TRAFFIC,
    SUDDEN_DROP,
    SUDDEN_SPIKE,
    UNUSUAL_NIGHT_TRAFFIC
}