package com.booknplay.turf.service;


import com.booknplay.turf.entity.Turf;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurfService {
        Turf submitOnboardingRequest(Turf turf);

    Turf getTurfById(Long turfId);

        List<Turf> getPendingRequests();

        Turf approveTurf(Long turfId, String adminRemark);

        Turf rejectTurf(Long turfId, String adminRemark);

        Turf updatePriceAndSchedule(
                Long turfId,
                BigDecimal basePrice,
                LocalTime openingTime,
                LocalTime closingTime
        );

        List<Turf> searchApprovedTurfs(
            String city,
            String area,
            String sport,
            LocalDate bookingDate,
            BigDecimal maxPrice
    );
    BigDecimal calculatePrice(
            Long turfId,
            LocalDate bookingDate,
            LocalTime startTime
    );

}
