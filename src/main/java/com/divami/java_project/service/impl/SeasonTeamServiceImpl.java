package com.divami.java_project.service.impl;

import com.divami.java_project.exception.BusinessException;
import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.SeasonTeam;
import com.divami.java_project.model.Team;
import com.divami.java_project.model.dto.SeasonTeamDTO;
import com.divami.java_project.repository.LeagueSeasonRepository;
import com.divami.java_project.repository.MatchRepository;
import com.divami.java_project.repository.SeasonTeamRepository;
import com.divami.java_project.repository.TeamRepository;
import com.divami.java_project.service.SeasonTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SeasonTeamServiceImpl implements SeasonTeamService {

    private static final Logger log = LoggerFactory.getLogger(SeasonTeamServiceImpl.class);

    private final SeasonTeamRepository seasonTeamRepository;
    private final LeagueSeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public SeasonTeamServiceImpl(SeasonTeamRepository seasonTeamRepository,
                                  LeagueSeasonRepository seasonRepository,
                                  TeamRepository teamRepository,
                                  MatchRepository matchRepository) {
        this.seasonTeamRepository = seasonTeamRepository;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public SeasonTeamDTO addTeamToSeason(UUID seasonId, UUID teamId) {
        if (seasonTeamRepository.existsBySeasonIdAndTeamIdAndDeletedAtIsNull(seasonId, teamId)) {
            throw new DuplicateResourceException("Team is already registered in this season");
        }
        LeagueSeason season = seasonRepository.findByIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", seasonId));
        Team team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", teamId));

        SeasonTeam seasonTeam = new SeasonTeam();
        seasonTeam.setSeason(season);
        seasonTeam.setTeam(team);
        seasonTeam.setCreatedAt(Instant.now());

        log.info("Adding teamId={} to seasonId={}", teamId, seasonId);
        return convertToDTO(seasonTeamRepository.save(seasonTeam));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeasonTeamDTO> findBySeason(UUID seasonId) {
        return seasonTeamRepository.findBySeasonIdAndDeletedAtIsNull(seasonId)
                .stream().map(this::convertToDTO).toList();
    }

    @Override
    public void removeFromSeason(UUID seasonTeamId) {
        SeasonTeam seasonTeam = seasonTeamRepository.findByIdAndDeletedAtIsNull(seasonTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("SeasonTeam", seasonTeamId));

        UUID teamId = seasonTeam.getTeam().getId();
        UUID seasonId = seasonTeam.getSeason().getId();

        boolean hasMatches = matchRepository.findBySeasonIdAndDeletedAtIsNull(seasonId, PageRequest.of(0, 1))
                .stream().anyMatch(m -> m.getHomeTeam().getId().equals(teamId)
                        || m.getAwayTeam().getId().equals(teamId));
        if (hasMatches) {
            throw new BusinessException("Cannot remove team from season — matches already exist for this team");
        }

        seasonTeam.setDeletedAt(Instant.now());
        seasonTeamRepository.save(seasonTeam);
        log.info("Removed seasonTeamId={} (teamId={} from seasonId={})", seasonTeamId, teamId, seasonId);
    }

    private SeasonTeamDTO convertToDTO(SeasonTeam st) {
        return new SeasonTeamDTO(st.getId(), st.getSeason().getId(),
                st.getTeam().getId(), st.getTeam().getName(), st.getTeam().getShortCode());
    }
}
