package com.divami.java_project.repository;

import com.divami.java_project.model.BulkCommunicationCampaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BulkCommunicationCampaignRepository extends JpaRepository<BulkCommunicationCampaign, UUID> {

    Page<BulkCommunicationCampaign> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
