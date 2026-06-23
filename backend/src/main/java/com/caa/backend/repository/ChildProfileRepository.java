package com.caa.backend.repository;

import com.caa.backend.model.ChildProfile;
import com.caa.backend.model.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildProfileRepository extends JpaRepository<ChildProfile, Long> {
    List<ChildProfile> findByTutorId(Long tutorId);
    Optional<ChildProfile> findByIdAndTutorId(Long id, Long tutorId);
    List<ChildProfile> findByLevel(Level level);
    List<ChildProfile> findByTutorIdAndLevel(Long tutorId, Level level);
    List<ChildProfile> findByNameContainingIgnoreCase(String name);
    boolean existsByIdAndTutorId(Long id, Long tutorId);

    @Query("SELECT c FROM ChildProfile c LEFT JOIN FETCH c.assignedBoards WHERE c.id = :id")
    Optional<ChildProfile> findByIdWithBoards(@Param("id") Long id);

    @Query("SELECT c FROM ChildProfile c LEFT JOIN FETCH c.assignedBoards WHERE c.id = :id AND c.tutor.id = :tutorId")
    Optional<ChildProfile> findByIdAndTutorIdWithBoards(@Param("id") Long id, @Param("tutorId") Long tutorId);

    @Query("SELECT DISTINCT c FROM ChildProfile c LEFT JOIN FETCH c.assignedBoards WHERE c.tutor.id = :tutorId")
    List<ChildProfile> findByTutorIdWithBoards(@Param("tutorId") Long tutorId);

    List<ChildProfile> findByTutorIdOrderByCreatedAtDesc(Long tutorId);

    @Query("SELECT COUNT(b) FROM ChildProfile c JOIN c.assignedBoards b WHERE c.id = :childProfileId")
    Long countAssignedBoardsByChildProfileId(@Param("childProfileId") Long childProfileId);
}

