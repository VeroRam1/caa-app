package com.caa.backend.controller;

import com.caa.backend.dto.ResponseDTOs.AdminTutorResponseDTO;
import com.caa.backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin", description = "Administrative endpoints — requires ROLE_ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/tutors")
    @Operation(summary = "Get all registered tutors",
            description = "Returns all tutors in the system with usage statistics. Admin only.")
    public ResponseEntity<List<AdminTutorResponseDTO>> getAllTutors(){
        log.info("GET /api/admin/tutors - Admin fetching tutor list");
        List<AdminTutorResponseDTO> tutors = adminService.getAllTutors();
        return ResponseEntity.ok(tutors);
    }
}
