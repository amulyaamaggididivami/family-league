package com.divami.java_project.service.impl;

import com.divami.java_project.model.PredictionPoints;
import com.divami.java_project.model.dto.PredictionPointsDTO;
import com.divami.java_project.repository.PredictionPointsRepository;
import com.divami.java_project.service.PredictionPointsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PredictionPointsServiceImpl implements PredictionPointsService {

    private final PredictionPointsRepository pointsRepository;

    public PredictionPointsServiceImpl(PredictionPointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    @Override
    public Page<PredictionPointsDTO> findByUserAndSeason(UUID userId, UUID seasonId, UUID matchId, Pageable pageable) {
        if (matchId != null) {
            return pointsRepository.findByUserIdAndSeasonIdAndMatchId(userId, seasonId, matchId, pageable)
                    .map(this::convertToDTO);
        }
        return pointsRepository.findByUserIdAndSeasonId(userId, seasonId, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<PredictionPointsDTO> findByMatch(UUID matchId, Pageable pageable) {
        return pointsRepository.findByMatchId(matchId, pageable).map(this::convertToDTO);
    }

    private PredictionPointsDTO convertToDTO(PredictionPoints p) {
        return new PredictionPointsDTO(
                p.getId(),
                p.getUser().getId(),
                p.getSeason().getId(),
                p.getMatch() != null ? p.getMatch().getId() : null,
                p.getMatchPrediction() != null ? p.getMatchPrediction().getId() : null,
                p.getLeaguePrediction() != null ? p.getLeaguePrediction().getId() : null,
                p.getPointsEarned(),
                p.getPointType(),
                p.getSourceField(),
                p.getCalculatedAt());
    }
}
