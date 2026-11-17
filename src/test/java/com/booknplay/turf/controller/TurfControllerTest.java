package com.booknplay.turf.controller;

import com.booknplay.turf.entity.Turf;
import com.booknplay.turf.service.TurfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class TurfControllerTest {

    private TurfService turfService;
    private TurfController turfController;

    @BeforeEach
    void setUp() {
        turfService = mock(TurfService.class);
        turfController = new TurfController(turfService);
    }

    @Test
    void submitOnboardingRequestReturnsServiceResult() {
        Turf turf = new Turf();
        when(turfService.submitOnboardingRequest(turf)).thenReturn(turf);

        ResponseEntity<Turf> response = turfController.submitOnboardingRequest(turf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turf, response.getBody());
        verify(turfService).submitOnboardingRequest(turf);
    }

    @Test
    void getPendingRequestsReturnsServiceResults() {
        List<Turf> turfs = List.of(new Turf(), new Turf());
        when(turfService.getPendingRequests()).thenReturn(turfs);

        ResponseEntity<List<Turf>> response = turfController.getPendingRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turfs, response.getBody());
        verify(turfService).getPendingRequests();
    }

    @Test
    void approveTurfPassesIdAndOptionalRemarkToService() {
        Turf turf = new Turf();
        when(turfService.approveTurf(7L, null)).thenReturn(turf);

        ResponseEntity<Turf> response = turfController.approveTurf(7L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turf, response.getBody());
        verify(turfService).approveTurf(7L, null);
    }

    @Test
    void rejectTurfPassesIdAndRemarkToService() {
        Turf turf = new Turf();
        when(turfService.rejectTurf(7L, "Incomplete details")).thenReturn(turf);

        ResponseEntity<Turf> response =
                turfController.rejectTurf(7L, "Incomplete details");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turf, response.getBody());
        verify(turfService).rejectTurf(7L, "Incomplete details");
    }

    @Test
    void updatePriceAndSchedulePassesAllValuesToService() {
        Turf turf = new Turf();
        BigDecimal price = new BigDecimal("75.50");
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(22, 0);
        when(turfService.updatePriceAndSchedule(
                7L, price, openingTime, closingTime)).thenReturn(turf);

        ResponseEntity<Turf> response = turfController.updatePriceAndSchedule(
                7L, price, openingTime, closingTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turf, response.getBody());
        verify(turfService).updatePriceAndSchedule(
                7L, price, openingTime, closingTime);
    }

    @Test
    void searchApprovedTurfsPassesAllCriteriaToService() {
        List<Turf> turfs = List.of(new Turf());
        LocalDate bookingDate = LocalDate.of(2026, 11, 21);
        BigDecimal maxPrice = new BigDecimal("100");
        when(turfService.searchApprovedTurfs(
                "Austin", "Downtown", "soccer", bookingDate, maxPrice))
                .thenReturn(turfs);

        ResponseEntity<List<Turf>> response = turfController.searchApprovedTurfs(
                "Austin", "Downtown", "soccer", bookingDate, maxPrice);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turfs, response.getBody());
        verify(turfService).searchApprovedTurfs(
                "Austin", "Downtown", "soccer", bookingDate, maxPrice);
    }

    @Test
    void getTurfByIdReturnsServiceResult() {
        Turf turf = new Turf();
        when(turfService.getTurfById(7L)).thenReturn(turf);

        ResponseEntity<Turf> response = turfController.getTurfById(7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(turf, response.getBody());
        verify(turfService).getTurfById(7L);
    }

    @Test
    void calculatePricePassesTurfIdDateAndStartTimeToService() {
        LocalDate bookingDate = LocalDate.of(2026, 11, 21);
        LocalTime startTime = LocalTime.of(18, 30);
        BigDecimal price = new BigDecimal("150.0");
        when(turfService.calculatePrice(7L, bookingDate, startTime))
                .thenReturn(price);

        ResponseEntity<BigDecimal> response =
                turfController.calculatePrice(7L, bookingDate, startTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(price, response.getBody());
        verify(turfService).calculatePrice(7L, bookingDate, startTime);
    }
}
