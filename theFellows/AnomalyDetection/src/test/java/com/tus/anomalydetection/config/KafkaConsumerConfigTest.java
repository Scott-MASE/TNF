package com.tus.anomalydetection.config;

import com.tus.anomalydetection.config.KafkaConsumerConfig;
import com.tus.common.dto.TrafficDataDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConsumerConfigTest {

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testTrafficDataConsumerFactory() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        setPrivateField(config, "bootstrapServers", "localhost:9092");

        ConsumerFactory<String, TrafficDataDTO> factory = config.trafficDataConsumerFactory();
        assertNotNull(factory);

        Map<String, Object> configProps = factory.getConfigurationProperties();
        assertEquals("localhost:9092", configProps.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("traffic-group", configProps.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(StringDeserializer.class, configProps.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals("earliest", configProps.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
    }

    @Test
    void testKafkaListenerContainerFactory() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        setPrivateField(config, "bootstrapServers", "localhost:9092");

        ConcurrentKafkaListenerContainerFactory<String, TrafficDataDTO> factory =
                config.kafkaListenerContainerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
    }
}
