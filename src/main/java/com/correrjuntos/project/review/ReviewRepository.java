package com.correrjuntos.project.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // CHECK REVIEW //

    @Query(value="SELECT * FROM review WHERE review.user_id = ?1 AND review.user_reviewed = ?2", nativeQuery = true)
    List<Map<String, Object>> checkReview(
        Integer user_id,
        Integer user_reviewed
    );

    // GET REVIEW //

    @Query(value="SELECT review.*, users.first_name FROM review INNER JOIN users ON users.id = review.user_id WHERE review.user_reviewed = ?1 ORDER BY review.created_at DESC", nativeQuery = true)
    List<Map<String, Object>> getReview(
        Integer user_id
    );

    // INSERT REVIEW //
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO review (comment, rate, user_id, user_reviewed, created_at, updated_at) VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertReview(
        String comment,
        Integer rate,
        Integer user_id,
        Integer user_reviewed,
        LocalDateTime created_at,
        LocalDateTime updated_at
    );
}
