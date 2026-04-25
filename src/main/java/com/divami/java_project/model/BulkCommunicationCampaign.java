package com.divami.java_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * An admin-initiated bulk email campaign sent to a filtered set of users.
 * Individual email dispatch records are stored in {@link EmailLog} with a reference
 * back to this campaign via campaign_id.
 */
@Entity
@Table(name = "bulk_communication_campaigns")
public class BulkCommunicationCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    /** Maps to email_event_types — e.g. PREDICTION_REMINDER, RESULT_UPDATE, ANNOUNCEMENT. */
    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "message_template", columnDefinition = "TEXT")
    private String messageTemplate;

    /** JSON criteria used to select recipients — stored as text, interpreted by the service layer. */
    @Column(name = "user_filter", columnDefinition = "jsonb")
    private String userFilter;

    @Column(name = "recipients_count")
    private Integer recipientsCount;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getMessageTemplate() { return messageTemplate; }
    public void setMessageTemplate(String messageTemplate) { this.messageTemplate = messageTemplate; }

    public String getUserFilter() { return userFilter; }
    public void setUserFilter(String userFilter) { this.userFilter = userFilter; }

    public Integer getRecipientsCount() { return recipientsCount; }
    public void setRecipientsCount(Integer recipientsCount) { this.recipientsCount = recipientsCount; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
}
