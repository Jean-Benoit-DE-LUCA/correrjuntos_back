package com.correrjuntos.project.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value="SELECT message.*, users.first_name FROM message INNER JOIN users ON users.id = message.user_id WHERE message.race_id = ?1 ORDER BY message.created_at ASC", nativeQuery = true)
    List<Map<String, Object>> getMessagesByRace(
        Integer race_id
    );




    @Query(value="SELECT * FROM races WHERE id = ?1", nativeQuery = true)
    List<Map<String, Object>> getRaceId(
        Integer race_id
    );



    
    
    @Transactional
    @Modifying
    @Query(value="INSERT INTO message (message, race_id, user_id, created_at, updated_at) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void insertMessage(
        String message,
        Integer race_id,
        Integer user_id,
        LocalDateTime created_at,
        LocalDateTime updated_at
    );
}
