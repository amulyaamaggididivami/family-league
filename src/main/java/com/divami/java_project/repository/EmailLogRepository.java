package com.divami.java_project.repository;

import com.divami.java_project.model.EmailLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, UUID> {

    Page<EmailLog> findByDeletedAtIsNull(Pageable pageable);

    Page<EmailLog> findByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    Page<EmailLog> findByEventTypeAndDeletedAtIsNull(String eventType, Pageable pageable);

    Page<EmailLog> findByUserIdAndEventTypeAndDeletedAtIsNull(UUID userId, String eventType, Pageable pageable);

    Optional<EmailLog> findByIdAndDeletedAtIsNull(UUID id);
}
