package dto;

import com.tus.common.dto.TrafficDataDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TrafficDataDTOTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        TrafficDataDTO dto = new TrafficDataDTO(10, 500, 1200.5, now);

        assertEquals(10, dto.getNodeId());
        assertEquals(500, dto.getNetworkId());
        assertEquals(1200.5, dto.getTrafficVolume());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        TrafficDataDTO dto = new TrafficDataDTO();

        dto.setId(1L);
        dto.setNodeId(20);
        dto.setNetworkId(600);
        dto.setTrafficVolume(980.0);
        dto.setTimestamp(now);

        assertEquals(1L, dto.getId());
        assertEquals(20, dto.getNodeId());
        assertEquals(600, dto.getNetworkId());
        assertEquals(980.0, dto.getTrafficVolume());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime time = LocalDateTime.now();

        TrafficDataDTO t1 = new TrafficDataDTO(1, 2, 3.0, time);
        t1.setId(100L);

        TrafficDataDTO t2 = new TrafficDataDTO(1, 2, 3.0, time);
        t2.setId(100L);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
