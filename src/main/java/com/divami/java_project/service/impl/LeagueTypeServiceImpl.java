package com.divami.java_project.service.impl;

import com.divami.java_project.exception.DuplicateResourceException;
import com.divami.java_project.exception.ResourceNotFoundException;
import com.divami.java_project.model.LeagueType;
import com.divami.java_project.model.dto.LeagueTypeDTO;
import com.divami.java_project.repository.LeagueTypeRepository;
import com.divami.java_project.service.LeagueTypeService;
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
public class LeagueTypeServiceImpl implements LeagueTypeService {

    private static final Logger log = LoggerFactory.getLogger(LeagueTypeServiceImpl.class);

    private final LeagueTypeRepository leagueTypeRepository;

    public LeagueTypeServiceImpl(LeagueTypeRepository leagueTypeRepository) {
        this.leagueTypeRepository = leagueTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeagueTypeDTO> findAll(Pageable pageable) {
        return leagueTypeRepository.findByDeletedAtIsNull(pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public LeagueTypeDTO findById(UUID id) {
        return convertToDTO(findOrThrow(id));
    }

    @Override
    public LeagueTypeDTO create(LeagueTypeDTO dto) {
        if (leagueTypeRepository.existsByNameAndDeletedAtIsNull(dto.name())) {
            throw new DuplicateResourceException("League type already exists: " + dto.name());
        }
        if (leagueTypeRepository.existsByShortCodeAndDeletedAtIsNull(dto.shortCode())) {
            throw new DuplicateResourceException("Short code already in use: " + dto.shortCode());
        }
        LeagueType entity = new LeagueType();
        mapFromDTO(dto, entity);
        entity.setCreatedAt(Instant.now());
        log.info("Creating league type name={}", dto.name());
        return convertToDTO(leagueTypeRepository.save(entity));
    }

    @Override
    public LeagueTypeDTO update(UUID id, LeagueTypeDTO dto) {
        LeagueType entity = findOrThrow(id);
        mapFromDTO(dto, entity);
        entity.setUpdatedAt(Instant.now());
        return convertToDTO(leagueTypeRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        LeagueType entity = findOrThrow(id);
        entity.setDeletedAt(Instant.now());
        leagueTypeRepository.save(entity);
        log.info("Soft-deleted leagueTypeId={}", id);
    }

    private LeagueType findOrThrow(UUID id) {
        return leagueTypeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeagueType", id));
    }

    private void mapFromDTO(LeagueTypeDTO dto, LeagueType entity) {
        entity.setName(dto.name());
        entity.setShortCode(dto.shortCode());
        entity.setCountry(dto.country());
        entity.setGender(dto.gender());
        entity.setFormat(dto.format());
        entity.setGoverningBody(dto.governingBody());
        entity.setDescription(dto.description());
        entity.setActive(dto.isActive());
    }

    private LeagueTypeDTO convertToDTO(LeagueType e) {
        return new LeagueTypeDTO(e.getId(), e.getName(), e.getShortCode(), e.getCountry(),
                e.getGender(), e.getFormat(), e.getGoverningBody(), e.getDescription(), e.isActive());
    }
}
