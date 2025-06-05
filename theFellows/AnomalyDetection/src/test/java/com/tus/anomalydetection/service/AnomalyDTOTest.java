package com.tus.anomalydetection.service;

import com.tus.common.dto.AnomalyDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyDTOTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AnomalyDTO dto = new AnomalyDTO(1, 100, "HighTraffic", 1234.56, now);

        assertEquals(1, dto.getNodeId());
        assertEquals(100, dto.getNetworkId());
        assertEquals("HighTraffic", dto.getAnomalyType());
        assertEquals(1234.56, dto.getTrafficVolume());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        AnomalyDTO dto = new AnomalyDTO();

        dto.setId(42L);
        dto.setNodeId(2);
        dto.setNetworkId(200);
        dto.setAnomalyType("LowTraffic");
        dto.setTrafficVolume(789.01);
        LocalDateTime timestamp = LocalDateTime.now();
        dto.setTimestamp(timestamp);

        assertEquals(42L, dto.getId());
        assertEquals(2, dto.getNodeId());
        assertEquals(200, dto.getNetworkId());
        assertEquals("LowTraffic", dto.getAnomalyType());
        assertEquals(789.01, dto.getTrafficVolume());
        assertEquals(timestamp, dto.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        AnomalyDTO dto1 = new AnomalyDTO(1, 100, "Spike", 555.55, now);
        dto1.setId(1L);

        AnomalyDTO dto2 = new AnomalyDTO(1, 100, "Spike", 555.55, now);
        dto2.setId(1L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
