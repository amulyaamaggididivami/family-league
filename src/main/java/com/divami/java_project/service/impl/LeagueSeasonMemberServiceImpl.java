package com.divami.java_project.service.impl;

import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeagueSeason;
import com.divami.java_project.model.LeagueSeasonMember;
import com.divami.java_project.model.User;
import com.divami.java_project.model.dto.LeagueSeasonMemberDTO;
import com.divami.java_project.repository.LeagueSeasonMemberRepository;
import com.divami.java_project.repository.LeagueSeasonRepository;
import com.divami.java_project.repository.UserRepository;
import com.divami.java_project.service.LeagueSeasonMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class LeagueSeasonMemberServiceImpl implements LeagueSeasonMemberService {

    private static final Logger log = LoggerFactory.getLogger(LeagueSeasonMemberServiceImpl.class);

    private final LeagueSeasonMemberRepository memberRepository;
    private final LeagueSeasonRepository seasonRepository;
    private final UserRepository userRepository;

    public LeagueSeasonMemberServiceImpl(LeagueSeasonMemberRepository memberRepository,
                                          LeagueSeasonRepository seasonRepository,
                                          UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.seasonRepository = seasonRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LeagueSeasonMemberDTO join(UUID seasonId, UUID userId) {
        if (memberRepository.existsBySeasonIdAndUserIdAndDeletedAtIsNull(seasonId, userId)) {
            throw new DuplicateResourceException("User is already a member of this season");
        }
        LeagueSeason season = seasonRepository.findByIdAndDeletedAtIsNull(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeason", seasonId));
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        LeagueSeasonMember member = new LeagueSeasonMember();
        member.setSeason(season);
        member.setUser(user);
        member.setStatus("ACTIVE");
        member.setJoinedAt(Instant.now());
        member.setCreatedAt(Instant.now());

        log.info("User {} joined seasonId={}", userId, seasonId);
        return convertToDTO(memberRepository.save(member));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeagueSeasonMemberDTO> findBySeason(UUID seasonId, Pageable pageable) {
        return memberRepository.findBySeasonIdAndDeletedAtIsNull(seasonId, pageable).map(this::convertToDTO);
    }

    @Override
    public void remove(UUID memberId) {
        LeagueSeasonMember member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueSeasonMember", memberId));
        member.setStatus("REMOVED");
        member.setDeletedAt(Instant.now());
        memberRepository.save(member);
        log.info("Removed memberId={}", memberId);
    }

    private LeagueSeasonMemberDTO convertToDTO(LeagueSeasonMember m) {
        return new LeagueSeasonMemberDTO(m.getId(), m.getSeason().getId(), m.getUser().getId(),
                m.getUser().getUsername(), m.getStatus(), m.getJoinedAt());
    }
}
