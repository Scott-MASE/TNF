package entity;

import com.tus.common.entity.Anomaly;
import com.tus.common.entity.AnomalyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Anomaly anomaly = new Anomaly(1, 100, AnomalyType.HIGH_TRAFFIC_VOLUME, 1234.56, now);

        assertEquals(1, anomaly.getNodeId());
        assertEquals(100, anomaly.getNetworkId());
        assertEquals(AnomalyType.HIGH_TRAFFIC_VOLUME, anomaly.getAnomalyType());
        assertEquals(1234.56, anomaly.getTrafficVolume());
        assertEquals(now, anomaly.getTimestamp());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Anomaly anomaly = new Anomaly();

        anomaly.setId(10L);
        anomaly.setNodeId(2);
        anomaly.setNetworkId(200);
        anomaly.setAnomalyType(AnomalyType.SUDDEN_DROP);
        anomaly.setTrafficVolume(789.01);
        anomaly.setTimestamp(now);

        assertEquals(10L, anomaly.getId());
        assertEquals(2, anomaly.getNodeId());
        assertEquals(200, anomaly.getNetworkId());
        assertEquals(AnomalyType.SUDDEN_DROP, anomaly.getAnomalyType());
        assertEquals(789.01, anomaly.getTrafficVolume());
        assertEquals(now, anomaly.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime time = LocalDateTime.now();

        Anomaly a1 = new Anomaly(1, 101, AnomalyType.SUDDEN_DROP, 222.2, time);
        a1.setId(1L);

        Anomaly a2 = new Anomaly(1, 101, AnomalyType.SUDDEN_DROP, 222.2, time);
        a2.setId(1L);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
