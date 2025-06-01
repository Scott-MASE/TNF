package com.tus.anomalydetection.repository;

import com.tus.common.entity.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {
    List<Anomaly> findByNetworkId(Integer networkId);
    boolean existsByNodeIdAndNetworkIdAndTimestamp(Integer nodeId, Integer networkId, LocalDateTime timestamp);
}

