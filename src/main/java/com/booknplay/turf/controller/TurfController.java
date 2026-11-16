package com.booknplay.turf.controller;


import com.booknplay.turf.entity.Turf;
import com.booknplay.turf.service.TurfService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/turfs")
public class TurfController {

    private final TurfService turfService;

    public TurfController(TurfService turfService) {
        this.turfService = turfService;

    }

    @PostMapping
    public ResponseEntity<Turf> submitOnboardingRequest(@RequestBody Turf turf) {
        return ResponseEntity.ok(turfService.submitOnboardingRequest(turf));

    }

    @GetMapping("/pending")
    public ResponseEntity<List<Turf>> getPendingRequests() {
        return ResponseEntity.ok(turfService.getPendingRequests());
    }

    @PutMapping("/{turfId}/approve")
    public ResponseEntity<Turf> approveTurf(@PathVariable Long turfId, @RequestParam(required = false) String adminRemark) {
        return ResponseEntity.ok(turfService.approveTurf(turfId, adminRemark));

    }

    @PutMapping("/{turfId}/reject")
    public ResponseEntity<Turf> rejectTurf(@PathVariable Long turfId, @RequestParam String adminRemark) {
        return ResponseEntity.ok(turfService.rejectTurf(turfId, adminRemark));

    }

    @PutMapping("/{turfId}/price-schedule")
    public ResponseEntity<Turf> updatePriceAndSchedule(@PathVariable Long turfId, @RequestParam BigDecimal basePrice, @RequestParam LocalTime openingTime, @RequestParam LocalTime closingTime) {
        return ResponseEntity.ok(turfService.updatePriceAndSchedule(
                        turfId, basePrice, openingTime, closingTime));

    }

    @GetMapping("/search")
    public ResponseEntity<List<Turf>> searchApprovedTurfs(
            @RequestParam String city, @RequestParam String area, @RequestParam String sport, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate bookingDate,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(
                turfService.searchApprovedTurfs(
                        city, area, sport, bookingDate, maxPrice
                )
        );
    }

    @GetMapping("/{turfId}")
    public ResponseEntity<Turf> getTurfById(
            @PathVariable Long turfId) {
        return ResponseEntity.ok(
                turfService.getTurfById(turfId)
        );
    }

}

