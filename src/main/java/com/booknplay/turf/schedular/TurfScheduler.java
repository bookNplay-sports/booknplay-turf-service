package com.booknplay.turf.schedular;


import com.booknplay.turf.entity.Turf;
import com.booknplay.turf.repository.TurfRepository;
import com.booknplay.turf.utility.TurfStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TurfScheduler {

    private final TurfRepository turfRepository;

    @Value("${turf.onboarding.expiry-days}")
    private int expiryDays;

    public TurfScheduler(TurfRepository turfRepository) {
        this.turfRepository = turfRepository;

    }

 //   @Scheduled(fixedRate = 6000)
    public void expirePendingTurfs() {

        LocalDateTime cutoff = LocalDateTime.now()
                .minusDays(expiryDays);
        List<Turf> expiredTurfs =
                turfRepository.findByStatusAndCreatedAtBefore(
                        TurfStatus.PENDING,
                        cutoff
                );

        for (Turf turf : expiredTurfs) {
            turf.setStatus(TurfStatus.EXPIRED);

        }

        turfRepository.saveAll(expiredTurfs);

    }

}

