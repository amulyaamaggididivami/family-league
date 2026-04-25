package com.divami.java_project.service.impl;

import com.divami.java_project.model.BulkCommunicationCampaign;
import com.divami.java_project.model.EmailLog;
import com.divami.java_project.model.User;
import com.divami.java_project.repository.EmailLogRepository;
import com.divami.java_project.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Value("${familyleague.mail.from}")
    private String fromAddress;

    public EmailNotificationServiceImpl(JavaMailSender mailSender,
                                         EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    public void sendPredictionReminder(User user, String matchDescription) {
        String subject = "Reminder: Submit your prediction for " + matchDescription;
        String body = "Hi " + user.getFullName() + ",\n\n"
                + "The prediction window for " + matchDescription + " is closing soon.\n"
                + "Log in and submit your prediction before the lock time.\n\nFamily League";
        dispatch(user, null, "PREDICTION_REMINDER", subject, body);
    }

    @Override
    public void sendResultPublishedAlert(User admin, String description) {
        String subject = "Result published: " + description;
        String body = "Hi " + admin.getFullName() + ",\n\n"
                + "The result for " + description + " has been published.\n"
                + "Points have been calculated and the leaderboard has been updated.\n\nFamily League";
        dispatch(admin, null, "RESULT_PUBLISHED", subject, body);
    }

    @Override
    public void sendBulkCampaignEmail(User recipient, BulkCommunicationCampaign campaign) {
        String body = campaign.getMessageTemplate() != null
                ? campaign.getMessageTemplate().replace("{name}", recipient.getFullName() != null ? recipient.getFullName() : recipient.getUsername())
                : "You have a message from Family League.";
        dispatch(recipient, campaign, campaign.getEventType(), "Family League — " + campaign.getEventType(), body);
    }

    private void dispatch(User user, BulkCommunicationCampaign campaign,
                          String eventType, String subject, String body) {
        EmailLog log_entry = new EmailLog();
        log_entry.setUser(user);
        log_entry.setCampaign(campaign);
        log_entry.setEventType(eventType);
        log_entry.setSubject(subject);
        log_entry.setCreatedAt(Instant.now());

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromAddress);
            msg.setTo(user.getEmail());
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);

            log_entry.setStatus("SENT");
            log_entry.setSentAt(Instant.now());
            log.info("Email sent to userId={} eventType={}", user.getId(), eventType);
        } catch (MailException e) {
            log_entry.setStatus("FAILED");
            log_entry.setErrorMessage(e.getMessage());
            log.error("Failed to send email to userId={} eventType={}: {}", user.getId(), eventType, e.getMessage());
        }

        emailLogRepository.save(log_entry);
    }
}
