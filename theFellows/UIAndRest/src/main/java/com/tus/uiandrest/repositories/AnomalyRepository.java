package com.tus.uiandrest.repositories;

import com.tus.common.entity.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {
    List<Anomaly> findByNetworkId(Integer networkId);
    List<Anomaly> findTop20ByOrderByTimestampDesc();
}

