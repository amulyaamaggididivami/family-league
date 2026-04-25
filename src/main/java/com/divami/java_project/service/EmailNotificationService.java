package com.divami.java_project.service;

import com.divami.java_project.model.BulkCommunicationCampaign;
import com.divami.java_project.model.User;

/**
 * Internal service for sending transactional and bulk emails.
 * Every email dispatched is recorded in the {@code email_logs} table.
 */
public interface EmailNotificationService {

    /** Sends a match prediction reminder to a user. Recorded as PREDICTION_REMINDER. */
    void sendPredictionReminder(User user, String matchDescription);

    /** Notifies an admin that a result was published and scoring is complete. */
    void sendResultPublishedAlert(User admin, String description);

    /** Sends one email as part of a bulk campaign. Recorded with the campaign_id. */
    void sendBulkCampaignEmail(User recipient, BulkCommunicationCampaign campaign);
}
