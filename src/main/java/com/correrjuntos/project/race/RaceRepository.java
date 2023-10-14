package com.correrjuntos.project.race;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long>{
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO races (city, race_date, number_users, race_duration, name_street, number_street, race_time, further_details, user_id) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
    void insertRace(
        String city,
        String race_date,
        Integer number_users,
        String race_duration,
        String name_street,
        String number_street,
        String race_time,
        String further_details,
        Integer user_id
    );
}
