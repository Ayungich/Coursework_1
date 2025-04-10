package com.ayungi.travelappserver.route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteMarkerRepository extends JpaRepository<RouteMarker, Long> {
    List<RouteMarker> findByTripId(Long tripId);
}

