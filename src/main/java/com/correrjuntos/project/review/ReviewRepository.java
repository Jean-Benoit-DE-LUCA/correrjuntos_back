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


    // FETCH REVIEWS //

    @Query(value="SELECT review.id AS review_id,\n" + //
                "\t   review.comment AS review_comment,\n" + //
                "\t   review.created_at AS review_created_at,\n" + //
                "\t   review.rate AS review_rate,\n" + //
                "\t   review.updated_at AS review_updated_at,\n" + //
                "\t   review.user_id AS review_user_id,\n" + //
                "\t   review.user_reviewed AS review_user_reviewed,\n" + //
                "\t   \n" + //
                "\t   users.id AS users_id,\n" + //
                "\t   users.email AS users_email,\n" + //
                "\t   users.first_name AS users_first_name,\n" + //
                "\t   users.last_name AS users_last_name,\n" + //
                "\t   users.password AS users_password,\n" + //
                "\t   users.birth_date AS users_birth_date,\n" + //
                "\t   users.city AS users_city,\n" + //
                "\t   users.street_name AS users_street_name,\n" + //
                "\t   users.street_number AS users_street_number,\n" + //
                "\t   users.zip_code AS users_zip_code,\n" + //
                "\t   users.gender AS users_gender,\n" + //
                "\t   users.picture AS users_picture\n" + //
                "FROM review\n" + //
                "INNER JOIN \n" + //
                "users ON users.id = review.user_reviewed\n" + //
                "WHERE review.user_id = ?1\n" + //
                "ORDER BY review.created_at DESC", nativeQuery = true)
    List<Map<String, Object>> fetchReviews(
        Integer user_id
    );



    // DELETE REVIEW //

    @Modifying
    @Transactional
    @Query(value="DELETE FROM review WHERE id = ?1", nativeQuery = true)
    void deleteReview(
        Integer review_id
    );
}
