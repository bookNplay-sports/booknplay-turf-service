package com.booknplay.turf.service;

import com.booknplay.turf.client.NotificationClient;
import com.booknplay.turf.dto.EmailNotificationRequest;
import com.booknplay.turf.entity.Turf;
import com.booknplay.turf.exception.InvalidTurfException;
import com.booknplay.turf.exception.TurfNotFoundException;
import com.booknplay.turf.repository.TurfRepository;
import com.booknplay.turf.utility.TurfStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TurfServiceImplTest {

    private TurfRepository turfRepository;
    private NotificationClient notificationClient;
    private TurfServiceImpl turfService;

    @BeforeEach
    void setUp() {
        turfRepository = mock(TurfRepository.class);
        notificationClient = mock(NotificationClient.class);
        turfService = new TurfServiceImpl(turfRepository, notificationClient);
    }

    @Test
    void submitOnboardingRequestSetsPendingStatusAndSavesTurf() {
        Turf turf = new Turf();
        when(turfRepository.save(turf)).thenReturn(turf);

        Turf result = turfService.submitOnboardingRequest(turf);

        assertSame(turf, result);
        assertEquals(TurfStatus.PENDING, turf.getStatus());
        verify(turfRepository).save(turf);
    }

    @Test
    void getTurfByIdReturnsTurfWhenFound() {
        Turf turf = new Turf();
        when(turfRepository.findById(7L)).thenReturn(Optional.of(turf));

        assertSame(turf, turfService.getTurfById(7L));
    }

    @Test
    void getTurfByIdThrowsWhenTurfDoesNotExist() {
        when(turfRepository.findById(7L)).thenReturn(Optional.empty());

        TurfNotFoundException exception = assertThrows(
                TurfNotFoundException.class,
                () -> turfService.getTurfById(7L)
        );

        assertEquals("Turf not found with id: 7", exception.getMessage());
    }

    @Test
    void getPendingRequestsReturnsPendingTurfs() {
        List<Turf> pendingTurfs = List.of(new Turf(), new Turf());
        when(turfRepository.findByStatus(TurfStatus.PENDING)).thenReturn(pendingTurfs);

        assertSame(pendingTurfs, turfService.getPendingRequests());
        verify(turfRepository).findByStatus(TurfStatus.PENDING);
    }

    @Test
    void approveTurfUpdatesStatusAndAdminRemark() {
        Turf turf = turfWithStatus(TurfStatus.PENDING);
        turf.setName("Central Turf");
        turf.setOwnerEmail("owner@example.com");
        when(turfRepository.findById(7L)).thenReturn(Optional.of(turf));
        when(turfRepository.save(turf)).thenReturn(turf);

        Turf result = turfService.approveTurf(7L, "Approved");

        assertSame(turf, result);
        assertEquals(TurfStatus.APPROVED, turf.getStatus());
        assertEquals("Approved", turf.getAdminRemark());
        verify(turfRepository).save(turf);
        verify(notificationClient).sendEmail(new EmailNotificationRequest(
                "owner@example.com",
                "Your Turf Has Been Approved",
                "ONBOARDING_STATUS",
                "Central Turf",
                "APPROVED",
                "Approved"
        ));
    }

    @Test
    void approveTurfRejectsNonPendingTurf() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithStatus(TurfStatus.APPROVED)));

        assertThrows(InvalidTurfException.class,
                () -> turfService.approveTurf(7L, "Approved"));

        verify(turfRepository, never()).save(any(Turf.class));
        verifyNoInteractions(notificationClient);
    }

    @Test
    void rejectTurfUpdatesStatusAndAdminRemark() {
        Turf turf = turfWithStatus(TurfStatus.PENDING);
        turf.setName("Central Turf");
        turf.setOwnerEmail("owner@example.com");
        when(turfRepository.findById(7L)).thenReturn(Optional.of(turf));
        when(turfRepository.save(turf)).thenReturn(turf);

        Turf result = turfService.rejectTurf(7L, "Incomplete details");

        assertSame(turf, result);
        assertEquals(TurfStatus.REJECTED, turf.getStatus());
        assertEquals("Incomplete details", turf.getAdminRemark());
        verify(turfRepository).save(turf);
        verify(notificationClient).sendEmail(new EmailNotificationRequest(
                "owner@example.com",
                "Your Turf Registration Has Been Rejected",
                "ONBOARDING_STATUS",
                "Central Turf",
                "REJECTED",
                "Incomplete details"
        ));
    }

    @Test
    void rejectTurfRejectsNonPendingTurf() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithStatus(TurfStatus.REJECTED)));

        assertThrows(InvalidTurfException.class,
                () -> turfService.rejectTurf(7L, "Incomplete details"));

        verify(turfRepository, never()).save(any(Turf.class));
        verifyNoInteractions(notificationClient);
    }

    @Test
    void updatePriceAndScheduleUpdatesAndSavesTurf() {
        Turf turf = new Turf();
        when(turfRepository.findById(7L)).thenReturn(Optional.of(turf));
        when(turfRepository.save(turf)).thenReturn(turf);
        BigDecimal price = new BigDecimal("75.50");
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(22, 0);

        Turf result = turfService.updatePriceAndSchedule(
                7L, price, openingTime, closingTime);

        assertSame(turf, result);
        assertEquals(price, turf.getBasePrice());
        assertEquals(openingTime, turf.getOpeningTime());
        assertEquals(closingTime, turf.getClosingTime());
        verify(turfRepository).save(turf);
    }

    @Test
    void updatePriceAndScheduleRejectsNullZeroAndNegativePrices() {
        Turf turf = new Turf();
        when(turfRepository.findById(7L)).thenReturn(Optional.of(turf));
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(22, 0);

        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(
                        7L, null, openingTime, closingTime));
        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(
                        7L, BigDecimal.ZERO, openingTime, closingTime));
        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(
                        7L, new BigDecimal("-1"), openingTime, closingTime));

        verify(turfRepository, never()).save(any(Turf.class));
    }

    @Test
    void updatePriceAndScheduleRejectsNullOrInvalidTimes() {
        when(turfRepository.findById(7L)).thenReturn(Optional.of(new Turf()));
        BigDecimal price = new BigDecimal("75.50");
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(22, 0);

        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(7L, price, null, closingTime));
        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(7L, price, openingTime, null));
        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(7L, price, openingTime, openingTime));
        assertThrows(IllegalArgumentException.class, () ->
                turfService.updatePriceAndSchedule(7L, price, closingTime, openingTime));

        verify(turfRepository, never()).save(any(Turf.class));
    }

    @Test
    void searchApprovedTurfsAppliesWeekendPriceMultiplier() {
        Turf affordable = turfWithPrice("80");
        Turf overBudget = turfWithPrice("81");
        List<Turf> approvedTurfs = List.of(affordable, overBudget);
        when(turfRepository
                .findByStatusAndCityIgnoreCaseAndAreaIgnoreCaseAndSportsContainingIgnoreCase(
                        TurfStatus.APPROVED, "Austin", "Downtown", "soccer"))
                .thenReturn(approvedTurfs);

        List<Turf> result = turfService.searchApprovedTurfs(
                "Austin", "Downtown", "soccer",
                LocalDate.of(2026, 11, 21), new BigDecimal("100"));

        assertEquals(List.of(affordable), result);
        verify(turfRepository)
                .findByStatusAndCityIgnoreCaseAndAreaIgnoreCaseAndSportsContainingIgnoreCase(
                        TurfStatus.APPROVED, "Austin", "Downtown", "soccer");
    }

    @Test
    void searchApprovedTurfsUsesBasePriceOnWeekday() {
        Turf withinBudget = turfWithPrice("100");
        Turf overBudget = turfWithPrice("100.01");
        when(turfRepository
                .findByStatusAndCityIgnoreCaseAndAreaIgnoreCaseAndSportsContainingIgnoreCase(
                        TurfStatus.APPROVED, "Austin", "Downtown", "soccer"))
                .thenReturn(List.of(withinBudget, overBudget));

        List<Turf> result = turfService.searchApprovedTurfs(
                "Austin", "Downtown", "soccer",
                LocalDate.of(2026, 11, 17), new BigDecimal("100"));

        assertEquals(List.of(withinBudget), result);
    }

    @Test
    void calculatePriceUsesBasePriceOnWeekdayOutsidePeakHours() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithPrice("100")));

        BigDecimal price = turfService.calculatePrice(
                7L, LocalDate.of(2026, 11, 17), LocalTime.of(17, 59));

        assertEquals(new BigDecimal("100"), price);
    }

    @Test
    void calculatePriceAppliesWeekendMultiplierOutsidePeakHours() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithPrice("100")));

        BigDecimal price = turfService.calculatePrice(
                7L, LocalDate.of(2026, 11, 21), LocalTime.of(17, 59));

        assertEquals(new BigDecimal("125.00"), price);
    }

    @Test
    void calculatePriceAppliesPeakHourMultiplierAtSixPm() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithPrice("100")));

        BigDecimal price = turfService.calculatePrice(
                7L, LocalDate.of(2026, 11, 17), LocalTime.of(18, 0));

        assertEquals(new BigDecimal("150.0"), price);
    }

    @Test
    void calculatePriceAppliesPeakMultiplierInsteadOfWeekendMultiplier() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithPrice("100")));

        BigDecimal price = turfService.calculatePrice(
                7L, LocalDate.of(2026, 11, 21), LocalTime.of(20, 59));

        assertEquals(new BigDecimal("150.0"), price);
    }

    @Test
    void calculatePriceUsesNonPeakRateAtNinePm() {
        when(turfRepository.findById(7L))
                .thenReturn(Optional.of(turfWithPrice("100")));

        BigDecimal price = turfService.calculatePrice(
                7L, LocalDate.of(2026, 11, 17), LocalTime.of(21, 0));

        assertEquals(new BigDecimal("100"), price);
    }

    @Test
    void calculatePriceThrowsWhenTurfDoesNotExist() {
        when(turfRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(TurfNotFoundException.class, () ->
                turfService.calculatePrice(
                        7L, LocalDate.of(2026, 11, 17), LocalTime.of(18, 0)));
    }

    private Turf turfWithStatus(TurfStatus status) {
        Turf turf = new Turf();
        turf.setStatus(status);
        return turf;
    }

    private Turf turfWithPrice(String price) {
        Turf turf = new Turf();
        turf.setBasePrice(new BigDecimal(price));
        return turf;
    }
}
