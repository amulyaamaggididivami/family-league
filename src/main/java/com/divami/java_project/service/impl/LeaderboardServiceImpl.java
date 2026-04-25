package com.divami.java_project.service.impl;

import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeaderboardSnapshot;
import com.divami.java_project.model.dto.LeaderboardSnapshotDTO;
import com.divami.java_project.repository.LeaderboardSnapshotRepository;
import com.divami.java_project.service.LeaderboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardSnapshotRepository snapshotRepository;

    public LeaderboardServiceImpl(LeaderboardSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    public List<LeaderboardSnapshotDTO> findCurrentBySeason(UUID seasonId) {
        Instant latest = snapshotRepository.findLatestSnapshotAt(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaderboardSnapshot for season", seasonId));
        return snapshotRepository.findBySeasonIdAndSnapshotAtOrderByRankAsc(seasonId, latest)
                .stream().map(this::convertToDTO).toList();
    }

    @Override
    public Page<LeaderboardSnapshotDTO> findSnapshotHistory(UUID seasonId, Pageable pageable) {
        return snapshotRepository.findBySeasonId(seasonId, pageable).map(this::convertToDTO);
    }

    private LeaderboardSnapshotDTO convertToDTO(LeaderboardSnapshot s) {
        return new LeaderboardSnapshotDTO(
                s.getId(),
                s.getSeason().getId(),
                s.getUser().getId(),
                s.getUser().getUsername(),
                s.getTotalPoints(),
                s.getRank(),
                s.getSnapshotAt());
    }
}
