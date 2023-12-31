package com.correrjuntos.project.race;

import jakarta.persistence.*;

@Entity
@Table(name="races")
public class Race {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String numberStreet;
    private String nameStreet;
    private String city;
    private String furtherDetails;
    private String raceDate;
    private String raceTime;
    private String raceDuration;
    private int numberUsers;
    private int userId;

    protected Race() {

    }

    public Race(
        String numberStreet,
        String nameStreet,
        String city,
        String furtherDetails,
        String raceDate,
        String raceTime,
        String raceDuration,
        int numberUsers
    ) {
        this.numberStreet = numberStreet;
        this.nameStreet = nameStreet;
        this.city = city;
        this.furtherDetails = furtherDetails;
        this.raceDate = raceDate;
        this.raceTime = raceTime;
        this.raceDuration = raceDuration;
        this.numberUsers = numberUsers;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberStreet() {
        return this.numberStreet;
    }

    public void setNumberStreet(String numberStreet) {
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

    public String getRaceTime() {
        return this.raceTime;
    }

    public void setRaceTime(String raceTime) {
        this.raceTime = raceTime;
    }

    public String getRaceDuration() {
        return this.raceDuration;
    }

    public void setRaceDuration(String raceDuration) {
        this.raceDuration = raceDuration;
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
}
