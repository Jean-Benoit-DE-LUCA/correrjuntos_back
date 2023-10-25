package com.correrjuntos.project.race;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long>{

    // INSERT RACE //
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO races (city, race_date, number_users, race_duration, name_street, number_street, race_time, further_details, user_id, created_at, updated_at) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11)", nativeQuery = true)
    void insertRace(
        String city,
        String race_date,
        Integer number_users,
        String race_duration,
        String name_street,
        Integer number_street,
        String race_time,
        String further_details,
        Integer user_id,
        LocalDateTime created_at,
        LocalDateTime updated_at
    );

    // GET LAST RACES //

    @Query(value="SELECT * FROM races ORDER BY races.created_at DESC LIMIT 5", nativeQuery = true)
    List<Race> getLastRaces();

    // GET RACE BY ID //

    @Query(value="SELECT races.*, users.first_name FROM races INNER JOIN users ON users.id = races.user_id WHERE races.id = ?1", nativeQuery = true)
    List<Map<Object, Object>> findRaceById(
        Long id
    );

    // GET USERS PARTICIPATE //

    @Query(value="SELECT users.first_name, race_user.* FROM users INNER JOIN race_user ON race_user.user_id = users.id WHERE race_user.race_id = ?1 ORDER BY race_user ASC", nativeQuery = true)
    List<Map<Object, Object>> getUsersParticipate(
        Long race_id
    );

    // GET LAST INSERTED RACE BY USER //

    @Query(value="SELECT * FROM races WHERE races.user_id = ?1 ORDER BY races.created_at DESC LIMIT 1", nativeQuery = true)
    List<Race> getLastInsertedRace(
        Integer user_id
    );

    // INSERT RACE_USER JUNCTION TABLE //

    @Modifying
    @Transactional
    @Query(value="INSERT INTO race_user (race_id, user_id, created_at, updated_at) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertRaceUser(
        Long race_id,
        Integer user_id,
        LocalDateTime created_at,
        LocalDateTime updated_at
    );

    // DELETE RACE_USER USER PARTICIPATE //

    @Modifying
    @Transactional
    @Query(value="DELETE FROM race_user WHERE race_user.race_id = ?1 AND race_user.user_id = ?2", nativeQuery = true)
    void deleteRaceUser(
        Long race_id,
        Integer user_id
    );
}
