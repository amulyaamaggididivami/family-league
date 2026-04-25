package com.divami.java_project.scheduler;

import com.divami.java_project.model.LeagueSeasonMember;
import com.divami.java_project.model.Match;
import com.divami.java_project.repository.LeagueSeasonMemberRepository;
import com.divami.java_project.repository.MatchPredictionRepository;
import com.divami.java_project.repository.MatchRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Scheduled jobs for time-driven email notifications (§5 of FamilyLeague.md).
 *
 * Prediction reminder: runs every hour.
 *   Finds matches starting within the configured window (default: 2 hours from now)
 *   where the prediction window is still open.
 *   Sends a reminder to each season member who has not yet submitted a prediction.
 */
@Component
public class NotificationScheduler {

    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);

    private final MatchRepository matchRepository;
    private final LeagueSeasonMemberRepository memberRepository;
    private final MatchPredictionRepository predictionRepository;
    private final EmailNotificationService emailService;

    @Value("${familyleague.notification.reminder-before-hours:2}")
    private long reminderBeforeHours;

    public NotificationScheduler(MatchRepository matchRepository,
                                   LeagueSeasonMemberRepository memberRepository,
                                   MatchPredictionRepository predictionRepository,
                                   EmailNotificationService emailService) {
        this.matchRepository = matchRepository;
        this.memberRepository = memberRepository;
        this.predictionRepository = predictionRepository;
        this.emailService = emailService;
    }

    /**
     * Every hour: find matches starting within the reminder window and email
     * any member who has not yet submitted a prediction.
     * Cron: "0 0 * * * *" = top of every hour.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional(readOnly = true)
    public void sendPredictionReminders() {
        Instant now = Instant.now();
        Instant windowEnd = now.plus(reminderBeforeHours, ChronoUnit.HOURS);

        List<Match> upcomingMatches = matchRepository.findScheduledBetween(now, windowEnd);
        if (upcomingMatches.isEmpty()) return;

        log.info("Scheduler: found {} upcoming matches needing prediction reminders", upcomingMatches.size());

        for (Match match : upcomingMatches) {
            // Skip if prediction window already closed
            if (now.isAfter(match.getPredictionLockTime())) continue;

            UUID seasonId = match.getSeason().getId();
            String description = match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName();

            Set<UUID> alreadyPredicted = Set.copyOf(
                    predictionRepository.findUserIdsWithPredictionForMatch(match.getId()));

            List<LeagueSeasonMember> members = memberRepository.findBySeasonIdAndDeletedAtIsNull(seasonId);

            for (LeagueSeasonMember member : members) {
                if (!alreadyPredicted.contains(member.getUser().getId())) {
                    try {
                        emailService.sendPredictionReminder(member.getUser(), description);
                    } catch (Exception e) {
                        log.warn("Scheduler: failed to send reminder to userId={}: {}",
                                member.getUser().getId(), e.getMessage());
                    }
                }
            }
        }
    }
}
