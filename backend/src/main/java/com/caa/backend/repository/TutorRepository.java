package com.caa.backend.repository;

import com.caa.backend.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Tutor> findByName(String name);
    List<Tutor> findByNameContainingIgnoreCase(String name);

    @Query("SELECT t FROM Tutor t LEFT JOIN FETCH t.childProfiles WHERE t.id = :id")
    Optional<Tutor> findByIdWithChildProfiles(@Param("id") Long id);

    @Query("SELECT t FROM Tutor t LEFT JOIN FETCH t.childProfiles WHERE t.email = :email")
    Optional<Tutor> findByEmailWithChildProfiles(@Param("email") String email);

    List<Tutor> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(c) FROM ChildProfile c WHERE c.tutor.id = :tutorId")
    Long countChildProfilesByTutorId(@Param("tutorId") Long tutorId);
}
