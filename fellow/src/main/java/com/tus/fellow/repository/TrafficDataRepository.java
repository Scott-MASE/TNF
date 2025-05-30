package com.tus.fellow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.fellow.entity.TrafficData;

@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {

	List<TrafficData> findByNodeId(Integer nodeId);
}
