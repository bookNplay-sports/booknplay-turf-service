package com.booknplay.turf.entity;


import com.booknplay.turf.utility.TurfStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
@Table(name = "turfs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Turf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String area;
    private String sports;
    private String ownerEmail;
    private BigDecimal basePrice;
    private LocalTime openingTime;
    private LocalTime closingTime;
    @Enumerated(EnumType.STRING)
    private TurfStatus status;
    private String adminRemark;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}