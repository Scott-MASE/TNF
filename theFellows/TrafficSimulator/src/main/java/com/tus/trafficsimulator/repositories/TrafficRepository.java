package com.tus.trafficsimulator.repositories;

import com.tus.trafficsimulator.entities.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficRepository extends JpaRepository<Traffic, Integer> {


}
