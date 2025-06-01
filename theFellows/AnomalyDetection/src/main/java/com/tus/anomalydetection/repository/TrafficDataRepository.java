package com.tus.anomalydetection.repository;

import com.tus.common.entity.TrafficData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {

	List<TrafficData> findByNodeId(Integer nodeId);
	
	@Query("SELECT t FROM TrafficData t WHERE t.timestamp >= :from")
	List<TrafficData> findRecentTrafficData(@Param("from") LocalDateTime from);
	
}
