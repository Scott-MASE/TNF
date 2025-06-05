package entity;

import com.tus.common.entity.TrafficData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TrafficDataTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        TrafficData data = new TrafficData(5, 200, 1500.75, now);

        assertEquals(5, data.getNodeId());
        assertEquals(200, data.getNetworkId());
        assertEquals(1500.75, data.getTrafficVolume());
        assertEquals(now, data.getTimestamp());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        TrafficData data = new TrafficData();
        LocalDateTime timestamp = LocalDateTime.now();

        data.setId(99L);
        data.setNodeId(3);
        data.setNetworkId(300);
        data.setTrafficVolume(999.99);
        data.setTimestamp(timestamp);

        assertEquals(99L, data.getId());
        assertEquals(3, data.getNodeId());
        assertEquals(300, data.getNetworkId());
        assertEquals(999.99, data.getTrafficVolume());
        assertEquals(timestamp, data.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime time = LocalDateTime.now();

        TrafficData t1 = new TrafficData(10, 400, 800.0, time);
        t1.setId(1L);

        TrafficData t2 = new TrafficData(10, 400, 800.0, time);
        t2.setId(1L);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
