package com.divami.java_project.model;

import com.divami.java_project.model.base.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * Master definition of a tournament type, e.g. IPL, WPL, BBL.
 * Reusable across multiple {@link League} editions (IPL 2024, IPL 2025, etc.).
 */
@Entity
@Table(name = "league_types")
public class LeagueType extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Column(name = "country")
    private String country;

    /** M = Men, W = Women, Mixed. */
    @Column(name = "gender")
    private String gender;

    /** e.g. T20, ODI, Test. */
    @Column(name = "format")
    private String format;

    @Column(name = "governing_body")
    private String governingBody;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getGoverningBody() { return governingBody; }
    public void setGoverningBody(String governingBody) { this.governingBody = governingBody; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
