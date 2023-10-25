package com.correrjuntos.project.race_user;

import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Race_User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long race_id;
    private Long user_id;
    private Date createdAt;
    private Date updatedAt;

    public Race_User() {

    }

    public Race_User(Long race_id, Long user_id, Date createdAt, Date updatedAt) {
        this.race_id = race_id;
        this.user_id = user_id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
