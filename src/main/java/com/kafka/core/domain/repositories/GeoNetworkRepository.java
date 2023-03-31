package com.kafka.core.domain.repositories;

import com.kafka.core.models.GeoNetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoNetworkRepository extends JpaRepository<GeoNetModel, Long> {
}
