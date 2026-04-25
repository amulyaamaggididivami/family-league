package com.divami.java_project.controller;

import com.divami.java_project.model.dto.LeagueSeasonMemberDTO;
import com.divami.java_project.service.LeagueSeasonMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/seasons/{seasonId}/members")
public class LeagueSeasonMemberController {

    private static final Logger log = LoggerFactory.getLogger(LeagueSeasonMemberController.class);

    private final LeagueSeasonMemberService memberService;

    public LeagueSeasonMemberController(LeagueSeasonMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<Page<LeagueSeasonMemberDTO>> findBySeason(
            @PathVariable UUID seasonId,
            @ParameterObject @PageableDefault(size = 20, sort = "joinedAt") Pageable pageable) {
        return ResponseEntity.ok(memberService.findBySeason(seasonId, pageable));
    }

    @PostMapping
    public ResponseEntity<LeagueSeasonMemberDTO> join(
            @PathVariable UUID seasonId,
            @RequestParam UUID userId) {
        log.info("User {} joining season {}", userId, seasonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.join(seasonId, userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> remove(
            @PathVariable UUID seasonId,
            @PathVariable UUID memberId) {
        log.info("Removing memberId={} from seasonId={}", memberId, seasonId);
        memberService.remove(memberId);
        return ResponseEntity.noContent().build();
    }
}
