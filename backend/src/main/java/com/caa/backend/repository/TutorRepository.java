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

    /**
     * Finds a tutor with all their child profiles loaded (avoids N+1 queries)
     * @param id tutor ID
     * @return Optional with the tutor and their child profiles
     */
    @Query("SELECT t FROM Tutor t LEFT JOIN FETCH t.childProfiles WHERE t.id = :id")
    Optional<Tutor> findByIdWithChildProfiles(@Param("id") Long id);

    /**
     * Finds a tutor with all their child profiles loaded by email
     * Used after login to return full tutor data
     * @param email tutor email
     * @return Optional with the tutor and their child profiles
     */
    @Query("SELECT t FROM Tutor t LEFT JOIN FETCH t.childProfiles WHERE t.email = :email")
    Optional<Tutor> findByEmailWithChildProfiles(@Param("email") String email);

    List<Tutor> findAllByOrderByCreatedAtDesc();

    /**
     * Counts the number of child profiles belonging to a tutor
     * @param tutorId tutor ID
     * @return number of child profiles
     */
    @Query("SELECT COUNT(c) FROM ChildProfile c WHERE c.tutor.id = :tutorId")
    Long countChildProfilesByTutorId(@Param("tutorId") Long tutorId);
}
