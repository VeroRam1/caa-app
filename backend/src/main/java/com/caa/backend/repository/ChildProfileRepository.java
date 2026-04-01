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

    /**
     * Finds a child profile by ID ensuring it belongs to the given tutor
     * Prevents tutors from accessing other tutors' children
     * @param id child profile ID
     * @param tutorId authenticated tutor's ID
     * @return Optional with the profile if it exists and belongs to this tutor
     */
    Optional<ChildProfile> findByIdAndTutorId(Long id, Long tutorId);
    List<ChildProfile> findByLevel(Level level);
    List<ChildProfile> findByTutorIdAndLevel(Long tutorId, Level level);
    List<ChildProfile> findByNameContainingIgnoreCase(String name);
    boolean existsByIdAndTutorId(Long id, Long tutorId);

    /**
     * Finds a child profile with all its assigned boards loaded (avoids N+1 queries)
     * @param id child profile ID
     * @return Optional with the child profile and its boards
     */
    @Query("SELECT c FROM ChildProfile c LEFT JOIN FETCH c.assignedBoards WHERE c.id = :id")
    Optional<ChildProfile> findByIdWithBoards(@Param("id") Long id);

    /**
     * Finds a child profile with boards, ensuring it belongs to the given tutor
     * Used in getProfileById to return full detail in a single query
     * @param id child profile ID
     * @param tutorId authenticated tutor's ID
     * @return Optional with the child profile and its boards
     */
    @Query("SELECT c FROM ChildProfile c LEFT JOIN FETCH c.assignedBoards WHERE c.id = :id AND c.tutor.id = :tutorId")
    Optional<ChildProfile> findByIdAndTutorIdWithBoards(@Param("id") Long id, @Param("tutorId") Long tutorId);

    /**
     * Finds all child profiles of a tutor with their boards loaded
     * @param tutorId tutor ID
     * @return list of child profiles with boards
     */
    @Query("SELECT DISTINCT c FROM ChildProfile c LEFT JOIN FETCH c.assignedBoards WHERE c.tutor.id = :tutorId")
    List<ChildProfile> findByTutorIdWithBoards(@Param("tutorId") Long tutorId);

    List<ChildProfile> findByTutorIdOrderByCreatedAtDesc(Long tutorId);

    /**
     * Counts the number of boards assigned to a child profile
     * @param childProfileId child profile ID
     * @return number of assigned boards
     */
    @Query("SELECT COUNT(b) FROM ChildProfile c JOIN c.assignedBoards b WHERE c.id = :childProfileId")
    Long countAssignedBoardsByChildProfileId(@Param("childProfileId") Long childProfileId);
}

