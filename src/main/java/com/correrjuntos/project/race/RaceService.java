package com.correrjuntos.project.race;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RaceService {
    
    private RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public Map<String, Boolean> jwtResponse(Map<String, Object> checkJWTresponse) {

        Object getJWTdetails = checkJWTresponse.get("tokenCheckResult");

        Map<String, Object> getToken = new HashMap<String, Object>();

        try {

            for (Field field : getJWTdetails.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    getToken.put(field.getName(), field.get(getJWTdetails));

                }

                catch (IllegalAccessException e) {
                    getToken.put("error", e.getMessage());
                }
            }
        }

        catch (InaccessibleObjectException e) {
            getToken.put("error", e.getMessage());
        }

        //--//

        Map<String, Boolean> getTokenPayloadValues = new HashMap<String, Boolean>();

        if (getToken.containsKey("payload")) {

            getTokenPayloadValues.put("tokenExpires", false);
        }

        else {

            getTokenPayloadValues.put("tokenExpires", true);
        }

        return getTokenPayloadValues;
    }

    // INSERT RACE //

    public Race insertRace(
        Boolean inputCheckBoxNoLimit,
        String inputCity,
        String inputDate,
        Integer inputMaxUsers,
        String inputRaceTime,
        String inputStreetName,
        Integer inputStreetNumber,
        LocalTime selectHour,
        String selectLevel,
        String textAreaFurtherDetails,
        String userMail,
        Integer userId
    ) {

        int maxUsers = 0;

        if (inputCheckBoxNoLimit) {

            maxUsers = -1;
        }

        else if (!inputCheckBoxNoLimit) {

            maxUsers = inputMaxUsers;
        }

        ZonedDateTime dateNow = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime created_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);
        LocalDateTime updated_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);

        raceRepository.insertRace(
            inputCity,
            inputDate,
            maxUsers,
            inputRaceTime,
            inputStreetName,
            inputStreetNumber,
            selectHour,
            selectLevel,
            textAreaFurtherDetails,
            userId,
            created_at,
            updated_at
        );

        return raceRepository.getLastInsertedRace(userId).get(0);
    }

    // UPDATE RACE //

    public void updateRace(
        String inputCity,
        String inputDate,
        int inputMaxUsers,
        String inputRaceTime,
        String inputStreetName,
        int inputStreetNumber,
        LocalTime selectHour,
        String selectLevel,
        String textAreaFurtherDetails,
        Long id
    ) {

        ZonedDateTime dateNow = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime updated_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);

        raceRepository.updateRace(inputCity, inputDate, inputMaxUsers, inputRaceTime, inputStreetName, inputStreetNumber, selectHour, selectLevel, textAreaFurtherDetails, updated_at, id);
    }

    // INSERT RACE_USER //

    public void insertRaceUser(
        Long race_id,
        Integer user_id
    ) {

        ZonedDateTime dateNow = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime created_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);
        LocalDateTime updated_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);

        raceRepository.insertRaceUser(
            race_id,
            user_id,
            created_at,
            updated_at
        );
    }

    // GET LAST RACES //

    public Map<String, Map<String, Object>> getLastRaces() {

        List<Race> getList = raceRepository.getLastRaces();

        Map<String, Map<String, Object>> newObj = new HashMap<String, Map<String, Object>>();
        Integer count = 0;

        for (Race obj : getList) {

            Map<String, Object> keyValues = new HashMap<String, Object>();

            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (field.getName() != "createdAt") {

                    try {

                        keyValues.put(field.getName(), field.get(obj));
                    }
                    catch (IllegalAccessException e) {

                        keyValues.put(field.getName(), e.getMessage());
                    }
                }

                else if (field.getName() == "createdAt") {
                    
                    try {

                        Object createdAtDateFormat = field.get(obj);

                        keyValues.put(field.getName(), createdAtDateFormat.toString().substring(0, createdAtDateFormat.toString().indexOf(".")));
                    }

                    catch (IllegalAccessException e) {

                        keyValues.put(field.getName(), e.getMessage());
                    }
                    
                }

            }

            newObj.put(String.valueOf(count), keyValues);
            count++;

        }

        return newObj;
    }

    // GET ALL RACES BY USER //

    public Map<String, Map<String, Object>> findAllRacesByUser(Long user_id) {

        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();

        List<Map<Object, Object>> listResult = raceRepository.findAllRacesByUserId(user_id);

        for (int i = 0; i < listResult.size(); i++) {

            Map<String, Object> raceKeyValue = new HashMap<String, Object>();

            for (Object key : listResult.get(i).keySet()) {

                raceKeyValue.put((String)key, listResult.get(i).get(key));
            }

            result.put(Integer.toString(i), raceKeyValue);
        }


        return result;
    }

    // GET RACE BY ID //

    public List<Map<Object, Object>> findRaceById(Long race_id) {

        List<Map<Object, Object>> getRaceById = raceRepository.findRaceById(race_id);

        return getRaceById;
    }

    // GET USERS PARTICIPATE //

    public Map<String, Map<Object, Object>> getUsersParticipate(Long race_id) {

        Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();

        List<Map<Object, Object>> getUsersParticipate = raceRepository.getUsersParticipate(race_id);

        for (int ind = 0; ind < getUsersParticipate.size(); ind++) {

            Map<Object, Object> newObj = new HashMap<Object, Object>();

            for (Object key : getUsersParticipate.get(ind).keySet()) {

                newObj.put(key, getUsersParticipate.get(ind).get(key));
            }

            result.put(String.valueOf(ind), newObj);
        }

        return result;
    }

    // GET COUNT USERS PARTICIPATE //

    public List<Map<String, Object>> getCountUsersParticipate() {

        return raceRepository.getCountUsersParticipate();
    }

    // PARTICIPATE RACE //

    public List<Map<Object, Object>> participateRace(
        Long race_id
    ) {

        return raceRepository.findRaceById(race_id);
    }

    // DELETE RACE_USER USER PARTICIPATE //

    public void deleteRaceUser(
        Long race_id,
        Integer user_id
    ) {

        raceRepository.deleteRaceUser(race_id, user_id);
    }

    // DELETE RACE //

    public void deleteRace(
        Long race_id
    ) {
        raceRepository.deleteRace(race_id);
        raceRepository.deleteAllRaceUserByRaceId(race_id);
    }
}
