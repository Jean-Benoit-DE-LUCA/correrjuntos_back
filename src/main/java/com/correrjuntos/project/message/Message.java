package com.correrjuntos.project.message;

import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Message {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long race_id;
    private Long user_id;
    private String message;
    private Date createdAt;
    private Date updatedAt;

    public Message() {

    }

    public Message(
        Long race_id, 
        Long user_id,
        String message
    ) {
        this.race_id = race_id;
        this.user_id = user_id;
        this.message = message;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRaceId() {
        return this.race_id;
    }

    public void setRaceId(Long race_id) {
        this.race_id = race_id;
    }

    public Long getUserId() {
        return this.user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
