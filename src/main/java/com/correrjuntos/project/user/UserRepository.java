package com.correrjuntos.project.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO users (first_name, last_name, email, birth_date, gender, street_number, street_name, zip_code, city, password) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10);", nativeQuery = true)
    void insertUser(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        String gender,
        Integer streetNumber,
        String streetName,
        Integer zipCode,
        String city,
        String password
    );

    @Query(value="SELECT * FROM users WHERE users.email = ?1", nativeQuery = true)
    List<User> findUserByMail(String email);

    @Query(value="SELECT * FROM users WHERE users.id = ?1", nativeQuery = true)
    List<User> findUserById(Integer user_id);
}