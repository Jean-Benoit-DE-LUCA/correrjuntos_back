package com.correrjuntos.project.review;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Review {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Integer rate;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Integer userId;
    private Integer userReviewed;

    private Date createdAt;
    private Date updatedAt;

    protected Review() {

    }

    public Review(
        Integer rate,
        String comment,
        Integer userId,
        Integer userReviewed,
        Date createdAt,
        Date updatedAt
    ) {

        this.rate = rate;
        this.comment = comment;
        this.userId = userId;
        this.userReviewed = userReviewed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return this.rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserReviewed() {
        return this.userReviewed;
    }

    public void setUserReviewed(Integer userReviewed) {
        this.userReviewed = userReviewed;
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
