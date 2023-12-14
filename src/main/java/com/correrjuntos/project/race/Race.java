package com.correrjuntos.project.race;

import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name="races")
public class Race {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private int numberStreet;
    private String nameStreet;
    private String city;
    private String furtherDetails;
    private String raceDate;
    private LocalTime raceTime;
    private String raceDuration;
    private String raceLevel;
    private int numberUsers;
    private int userId;
    private Date createdAt;
    private Date updatedAt;

    protected Race() {

    }

    public Race(
        int numberStreet,
        String nameStreet,
        String city,
        String furtherDetails,
        String raceDate,
        LocalTime raceTime,
        String raceDuration,
        int numberUsers,
        Date createdAt,
        Date updatedAt
    ) {
        this.numberStreet = numberStreet;
        this.nameStreet = nameStreet;
        this.city = city;
        this.furtherDetails = furtherDetails;
        this.raceDate = raceDate;
        this.raceTime = raceTime;
        this.raceDuration = raceDuration;
        this.numberUsers = numberUsers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberStreet() {
        return this.numberStreet;
    }

    public void setNumberStreet(int numberStreet) {
        this.numberStreet = numberStreet;
    }

    public String getNameStreet() {
        return this.nameStreet;
    }

    public void setNameStreet(String nameStreet) {
        this.nameStreet= nameStreet;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFurtherDetails() {
        return this.furtherDetails;
    }

    public void setFurtherDetails(String furtherDetails) {
        this.furtherDetails = furtherDetails;
    }

    public String getRaceDate() {
        return this.raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public LocalTime getRaceTime() {
        return this.raceTime;
    }

    public void setRaceTime(LocalTime raceTime) {
        this.raceTime = raceTime;
    }

    public String getRaceDuration() {
        return this.raceDuration;
    }

    public void setRaceDuration(String raceDuration) {
        this.raceDuration = raceDuration;
    }

    public String getRaceLevel() {
        return this.raceLevel;
    }

    public void setRaceLevel(String raceLevel) {
        this.raceLevel = raceLevel;
    }

    public int getNumberUsers() {
        return this.numberUsers;
    }

    public void setNumberUsers(int numberUsers) {
        this.numberUsers = numberUsers;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt= createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
