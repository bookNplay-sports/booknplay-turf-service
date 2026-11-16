package com.booknplay.turf.controller;


import com.booknplay.turf.entity.Turf;
import com.booknplay.turf.service.TurfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/turfs")
@Tag(
        name = "Turf Management",
        description = "APIs for turf onboarding, approval, search and management"
)
public class TurfController {

    private final TurfService turfService;

    public TurfController(TurfService turfService) {
        this.turfService = turfService;

    }

    @PostMapping
    @Operation(
            summary = "Submit a new turf",
            description = "Allows a turf owner to submit turf details for admin approval."
    )
    public ResponseEntity<Turf> submitOnboardingRequest(@RequestBody Turf turf) {
        return ResponseEntity.ok(turfService.submitOnboardingRequest(turf));

    }

    @GetMapping("/pending")
    @Operation(
            summary = "Get pending turf requests",
            description = "Retrieves all turf requests that are pending admin approval."
    )
    public ResponseEntity<List<Turf>> getPendingRequests() {
        return ResponseEntity.ok(turfService.getPendingRequests());
    }

    @PutMapping("/{turfId}/approve")
    @Operation(
            summary = "Approve a turf",
            description = "Approves a turf request for admin approval."
    )
    public ResponseEntity<Turf> approveTurf(@PathVariable Long turfId, @RequestParam(required = false) String adminRemark) {
        return ResponseEntity.ok(turfService.approveTurf(turfId, adminRemark));

    }

    @PutMapping("/{turfId}/reject")
    @Operation(
            summary = "Reject a turf",
            description = "Rejects a turf request for admin approval."
    )
    public ResponseEntity<Turf> rejectTurf(@PathVariable Long turfId, @RequestParam String adminRemark) {
        return ResponseEntity.ok(turfService.rejectTurf(turfId, adminRemark));

    }

    @PutMapping("/{turfId}/price-schedule")
    @Operation(
            summary = "Update turf price and schedule",
            description = "Updates the price and operating schedule for a specific turf."
    )
    public ResponseEntity<Turf> updatePriceAndSchedule(@PathVariable Long turfId, @RequestParam BigDecimal basePrice, @RequestParam LocalTime openingTime, @RequestParam LocalTime closingTime) {
        return ResponseEntity.ok(turfService.updatePriceAndSchedule(
                        turfId, basePrice, openingTime, closingTime));

    }

    @GetMapping("/search")
    @Operation(
            summary = "Search approved turfs",
            description = "Searches for approved turfs based on specified criteria."
    )
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

    @Operation(
            summary = "Get turf by ID",
            description = "Retrieves details of a specific turf by its ID."
    )
    @GetMapping("/{turfId}")
    public ResponseEntity<Turf> getTurfById(
            @PathVariable Long turfId) {
        return ResponseEntity.ok(
                turfService.getTurfById(turfId)
        );
    }

}

