package com.correrjuntos.project.race;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.HashMap;
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

    public void insertRace(
        Boolean inputCheckBoxNoLimit,
        String inputCity,
        String inputDate,
        Integer inputMaxUsers,
        String inputRaceTime,
        String inputStreetName,
        String inputStreetNumber,
        String selectHour,
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

        raceRepository.insertRace(
            inputCity,
            inputDate,
            maxUsers,
            inputRaceTime,
            inputStreetName,
            inputStreetNumber,
            selectHour,
            textAreaFurtherDetails,
            userId
        );
    }
}
