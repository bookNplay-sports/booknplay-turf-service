package com.booknplay.turf.service;

import com.booknplay.turf.entity.Turf;

import com.booknplay.turf.exception.InvalidTurfException;
import com.booknplay.turf.exception.TurfNotFoundException;
import com.booknplay.turf.repository.TurfRepository;
import com.booknplay.turf.utility.TurfStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Service
public class TurfServiceImpl implements TurfService {
    private final TurfRepository turfRepository;
    public TurfServiceImpl(TurfRepository turfRepository) {
        this.turfRepository = turfRepository;
    }
    @Override
    public Turf submitOnboardingRequest(Turf turf) {
        turf.setStatus(TurfStatus.PENDING);
        return turfRepository.save(turf);
    }
    @Override
    public Turf getTurfById(Long turfId) {
        return turfRepository.findById(turfId)
                .orElseThrow(() ->
                        new TurfNotFoundException("Turf not found with id: " + turfId)
                );
    }
    @Override
    public List<Turf> getPendingRequests() {
        return turfRepository.findByStatus(TurfStatus.PENDING);
    }
    @Override
    public Turf approveTurf(Long turfId, String adminRemark) {
        Turf turf = getTurfById(turfId);
        if (turf.getStatus() != TurfStatus.PENDING) {
            throw new InvalidTurfException(
                    "Only pending turf requests can be approved"
            );
        }
        turf.setStatus(TurfStatus.APPROVED);
        turf.setAdminRemark(adminRemark);
        return turfRepository.save(turf);
    }
    @Override
    public Turf rejectTurf(Long turfId, String adminRemark) {
        Turf turf = getTurfById(turfId);
        if (turf.getStatus() != TurfStatus.PENDING) {
            throw new InvalidTurfException(
                    "Only pending turf requests can be rejected"
            );
        }
        turf.setStatus(TurfStatus.REJECTED);
        turf.setAdminRemark(adminRemark);
        return turfRepository.save(turf);
    }
    @Override
    public Turf updatePriceAndSchedule(
            Long turfId,
            BigDecimal basePrice,
            LocalTime openingTime,
            LocalTime closingTime) {
        Turf turf = getTurfById(turfId);
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base price must be greater than zero");
        }
        if (openingTime == null || closingTime == null ||
                !openingTime.isBefore(closingTime)) {
            throw new IllegalArgumentException("Opening time must be before closing time");
        }
        turf.setBasePrice(basePrice);
        turf.setOpeningTime(openingTime);
        turf.setClosingTime(closingTime);
        return turfRepository.save(turf);
    }

    @Override

    public List<Turf> searchApprovedTurfs(

            String city,

            String area,

            String sport,

            LocalDate bookingDate,

            BigDecimal maxPrice) {

        List<Turf> turfs = turfRepository
                .findByStatusAndCityIgnoreCaseAndAreaIgnoreCaseAndSportsContainingIgnoreCase(
                        TurfStatus.APPROVED,
                        city,
                        area,
                        sport

                );

        BigDecimal multiplier = bookingDate.getDayOfWeek().getValue() >= 6

                ? new BigDecimal("1.25")

                : BigDecimal.ONE;

        return turfs.stream()

                .filter(turf -> turf.getBasePrice()

                        .multiply(multiplier)

                        .compareTo(maxPrice) <= 0)

                .toList();

    }

}