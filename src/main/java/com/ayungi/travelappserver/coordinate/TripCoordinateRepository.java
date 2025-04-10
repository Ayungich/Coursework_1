package com.ayungi.travelappserver.coordinate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripCoordinateRepository extends JpaRepository<TripCoordinate, Long> {
    List<TripCoordinate> findByTripId(Long tripId);
}
