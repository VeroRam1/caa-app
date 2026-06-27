package com.caa.backend.service;

import com.caa.backend.dto.ResponseDTOs.AdminTutorResponseDTO;
import com.caa.backend.model.Tutor;
import com.caa.backend.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {
    private final TutorRepository tutorRepository;

    public List<AdminTutorResponseDTO> getAllTutors() {
        log.info("Admin: fetching all registered tutors");

        List<Tutor> tutors = tutorRepository.findAll();

        return tutors.stream()
                .map(tutor -> AdminTutorResponseDTO.builder()
                        .id(tutor.getId())
                        .name(tutor.getName())
                        .email(tutor.getEmail())
                        .childProfileCount(tutor.getChildProfiles().size())
                        .ownedBoardCount(tutor.getOwnedBoards().size())
                        .createdAt(tutor.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
