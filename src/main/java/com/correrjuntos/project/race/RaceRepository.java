package com.correrjuntos.project.race;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long>{

    // INSERT RACE //
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO races (city, race_date, number_users, race_duration, name_street, number_street, race_time, race_level, only_male, only_female, further_details, user_id, created_at, updated_at) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14)", nativeQuery = true)
    void insertRace(
        String city,
        String race_date,
        Integer number_users,
        String race_duration,
        String name_street,
        Integer number_street,
        LocalTime race_time,
        String race_level,
        String only_male,
        String only_female,
        String further_details,
        Integer user_id,
        LocalDateTime created_at,
        LocalDateTime updated_at
    );

    // UPDATE RACE //

    @Modifying
    @Transactional
    @Query(value="UPDATE races SET city = ?1, race_date = ?2, number_users = ?3, race_duration = ?4, name_street = ?5, number_street = ?6, race_time = ?7, race_level = ?8, only_male = ?9, only_female = ?10, further_details = ?11, updated_at = ?12 WHERE id = ?13", nativeQuery = true)
    void updateRace(
        String city,
        String race_date,
        int number_users,
        String race_duration,
        String name_street,
        int number_street,
        LocalTime race_time,
        String race_level,
        String only_male,
        String only_female,
        String further_details,
        LocalDateTime updated_at,
        Long id
        
    );

    // GET ALL RACES BY USER //

    @Query(value="SELECT * FROM races WHERE races.user_id = ?1", nativeQuery = true)
    List<Map<Object, Object>> findAllRacesByUserId(
        Long user_id
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

    // GET COUNT USERS PARTICIPATE //

    @Query(value="SELECT races.id, COUNT(races.id) AS number_users_registered, races.number_users\n" + //
            "FROM race_user\n" + //
            "INNER JOIN races ON races.id = race_user.race_id\n" + //
            "GROUP BY races.id", nativeQuery = true)
    List<Map<String, Object>> getCountUsersParticipate();

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

    // DELETE RACE //

    @Modifying
    @Transactional
    @Query(value="DELETE FROM races WHERE races.id = ?1", nativeQuery = true)
    void deleteRace(
        Long race_id
    );

    // DELETE ALL RACE_USER BY RACE_ID //

    @Modifying
    @Transactional
    @Query(value="DELETE FROM race_user WHERE race_user.race_id = ?1", nativeQuery = true)
    void deleteAllRaceUserByRaceId(
        Long race_id
    );

    // DELETE ALL MESSAGE BY RACE_ID //

    @Modifying
    @Transactional
    @Query(value="DELETE FROM message WHERE message.race_id = ?1", nativeQuery = true)
    void deleteAllMessageByRaceId(
        Long race_id
    );

    // SEARCH RACES //

    @Query(value="SELECT *\n" + //
            "FROM\n" + //
            "(SELECT \n" + //
            "races.id as races_id,\n" + //
            "races.city as races_city,\n" + //
            "races.further_details as races_further_details,\n" + //
            "races.name_street as races_name_street,\n" + //
            "races.number_street as races_number_street,\n" + //
            "races.number_users as races_number_users,\n" + //
            "races.race_date as races_race_date,\n" + //
            "races.race_duration as races_race_duration,\n" + //
            "races.race_time as races_race_time,\n" + //
            "races.user_id as races_user_id,\n" + //
            "races.created_at as races_created_at,\n" + //
            "races.updated_at as races_updated_at,\n" + //
            "races.race_level as races_race_level,\n" + //
            "races.only_female as races_only_female,\n" + //
            "races.only_male as races_only_male,\n" + //
            "users.id as users_id,\n" + //
            "users.email as users_email,\n" + //
            "users.first_name as users_first_name,\n" + //
            "users.last_name as users_last_name,\n" + //
            "users.password as users_password,\n" + //
            "users.birth_date as users_birth_date,\n" + //
            "users.city as users_city,\n" + //
            "users.street_name as users_street_name,\n" + //
            "users.street_number as users_street_number,\n" + //
            "users.zip_code as users_zip_code,\n" + //
            "users.gender as users_gender,\n" + //
            "users.picture as users_picture,\n" + //
            "COUNT(race_user.race_id) as number_users_registered\n" + //
            "FROM races\n" + //
            "INNER JOIN users ON users.id = races.user_id\n" + //
            "INNER JOIN race_user ON race_user.race_id = races.id\n" + //
            "GROUP BY races.id, users.id) as query_one\n" + //
            "\n" + //
            "WHERE query_one.races_city ILIKE CONCAT('%', :cityToSearch, '%')\n" + //
            "AND query_one.users_first_name ILIKE CONCAT('%', :userToSearch, '%')\n" + //
            "AND\n" + //
            "(CASE\n" + //
            "\tWHEN LENGTH(:dateStartResult) > 0 AND LENGTH(:dateEndResult) = 0 THEN (query_one.races_race_date BETWEEN :dateStartResult AND '2099-12-31')\n" + //
            " \tWHEN LENGTH(:dateStartResult) = 0 AND LENGTH(:dateEndResult) > 0 THEN (query_one.races_race_date BETWEEN '1950-01-01' AND :dateEndResult)\n" + //
            " \tWHEN LENGTH(:dateStartResult) > 0 AND LENGTH(:dateEndResult) > 0 THEN (query_one.races_race_date BETWEEN :dateStartResult AND :dateEndResult)\n" + //
            " \tWHEN LENGTH(:dateStartResult) = 0 AND LENGTH (:dateEndResult) = 0 THEN (query_one.races_race_date BETWEEN '1950-01-01' AND '2099-12-31')\n" + //
            "END)\n"  + //
            "\n" + //
            "AND\n" + //
            "(CASE\n" + //
            "\tWHEN :hourStartResult = '00:00' AND :hourEndResult = '00:00' THEN (query_one.races_race_time BETWEEN CAST('00:00:00' AS TIME WITHOUT TIME ZONE) AND CAST('23:59:59' AS TIME WITHOUT TIME ZONE))\n" + //
            "\tWHEN LENGTH(:hourStartResult) > 0 AND LENGTH(:hourEndResult) > 0 THEN (query_one.races_race_time BETWEEN CAST(:hourStartResult AS TIME WITHOUT TIME ZONE) AND CAST(:hourEndResult AS TIME WITHOUT TIME ZONE))\n" + //
            " \tWHEN LENGTH(:hourStartResult) = 0 AND LENGTH(:hourEndResult) = 0 THEN (query_one.races_race_time BETWEEN CAST('00:00:00' AS TIME WITHOUT TIME ZONE) AND CAST('23:59:59' AS TIME WITHOUT TIME ZONE))\n" + //
            "END)\n" + //
            "ORDER BY\n" + //
            "races_race_date DESC;", nativeQuery = true)
    List<Map<String, Object>> searchRaces(
        @Param("cityToSearch") String cityToSearch,
        @Param("userToSearch") String userToSearch,
        @Param("dateStartResult") String dateStartResult,
        @Param("dateEndResult") String dateEndResult,
        @Param("hourStartResult") String hourStartResult,
        @Param("hourEndResult") String hourEndResult

    );





    // SEARCH RACE BY ID //

    @Query(value="SELECT \n" + //
                "races.id as races_id,\n" + //
                "races.city as races_city,\n" + //
                "races.further_details as races_further_details,\n" + //
                "races.name_street as races_name_street,\n" + //
                "races.number_street as races_number_street,\n" + //
                "races.number_users as races_number_users,\n" + //
                "races.race_date as races_race_date,\n" + //
                "races.race_duration as races_race_duration,\n" + //
                "races.race_time as races_race_time,\n" + //
                "races.user_id as races_user_id,\n" + //
                "races.created_at as races_created_at,\n" + //
                "races.updated_at as races_updated_at,\n" + //
                "races.race_level as races_race_level,\n" + //
                "races.only_female as races_only_female,\n" + //
                "races.only_male as races_only_male,\n" + //
                "users.id as users_id,\n" + //
                "users.email as users_email,\n" + //
                "users.first_name as users_first_name,\n" + //
                "users.last_name as users_last_name,\n" + //
                "users.password as users_password,\n" + //
                "users.birth_date as users_birth_date,\n" + //
                "users.city as users_city,\n" + //
                "users.street_name as users_street_name,\n" + //
                "users.street_number as users_street_number,\n" + //
                "users.zip_code as users_zip_code,\n" + //
                "users.gender as users_gender,\n" + //
                "users.picture as users_picture,\n" + //
                "COUNT(race_user.race_id) as number_users_registered\n" + //
                "FROM races\n" + //
                "INNER JOIN users ON users.id = races.user_id\n" + //
                "INNER JOIN race_user ON race_user.race_id = races.id\n" + //
                "GROUP BY races.id, users.id\n" + //
                "HAVING races.id = ?1", nativeQuery = true)
    List<Map<String, Object>> searchRaceById(
        Integer race_id
    );
}
