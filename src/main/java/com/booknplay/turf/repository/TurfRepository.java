package com.booknplay.turf.repository;

import com.booknplay.turf.entity.Turf;
import com.booknplay.turf.utility.TurfStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TurfRepository extends JpaRepository<Turf, Long> {
    List<Turf> findByStatus(TurfStatus status);

    List<Turf> findByStatusAndCityIgnoreCaseAndAreaIgnoreCaseAndSportsContainingIgnoreCase(
            TurfStatus status,
            String city,
            String area,
            String sport
    );
}