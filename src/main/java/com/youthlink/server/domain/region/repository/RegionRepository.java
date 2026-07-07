package com.youthlink.server.domain.region.repository;

import com.youthlink.server.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
