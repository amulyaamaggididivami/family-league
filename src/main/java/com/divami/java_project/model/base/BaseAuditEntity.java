package com.divami.java_project.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;

/**
 * Superclass for all auditable entities.
 * Provides created_at, created_by, updated_at, updated_by, deleted_at fields
 * inherited by every entity that participates in soft-delete and audit tracking.
 */
@MappedSuperclass
public abstract class BaseAuditEntity {

    /** Timestamp when the record was first persisted. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** ID of the user who created the record. */
    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    /** Timestamp of the most recent update. */
    @Column(name = "updated_at")
    private Instant updatedAt;

    /** ID of the user who last updated the record. */
    @Column(name = "updated_by")
    private UUID updatedBy;

    /**
     * Soft-delete marker. A non-null value means the record is logically deleted.
     * All queries must filter WHERE deleted_at IS NULL unless explicitly retrieving deleted records.
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
