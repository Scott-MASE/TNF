package com.tus.fellow.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tus.fellow.entity.TrafficData;

@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {

	List<TrafficData> findByNodeId(Integer nodeId);
	
	@Query("SELECT t FROM TrafficData t WHERE t.timestamp >= :from")
	List<TrafficData> findRecentTrafficData(@Param("from") LocalDateTime from);
	
}
