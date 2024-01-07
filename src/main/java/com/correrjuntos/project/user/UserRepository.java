package com.correrjuntos.project.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value="SELECT * FROM (SELECT users.* FROM users WHERE users.id = :user_id) AS query_one CROSS JOIN (SELECT CAST(AVG(rate) AS DECIMAL(10, 1)) AS average_rate FROM review WHERE user_reviewed = :user_id) AS query_two;", nativeQuery = true)
    List<Map<String, Object>> findUserById(@Param("user_id") Integer user_id);//map string object//

    @Modifying
    @Transactional
    @Query(value="UPDATE users\n" + //
            "SET \n" + //
            "first_name = ?1,\n" + //
            "last_name = ?2,\n" + //
            "email = ?3,\n" + //
            "birth_date = ?4,\n" + //
            "gender = ?5,\n" + //
            "street_number = ?6,\n" + //
            "street_name = ?7,\n" + //
            "zip_code = ?8,\n" + //
            "city = ?9,\n" + //
            "password = ?10,\n" + //
            "picture = ?11\n" + //
            "WHERE id = ?12", nativeQuery = true)
    void update(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        String gender,
        Integer streetNumber,
        String streetName,
        Integer zipCode,
        String city,
        String password,
        String picture,
        Integer id
    );
}