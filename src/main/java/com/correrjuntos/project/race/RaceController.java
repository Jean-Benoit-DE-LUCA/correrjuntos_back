package com.correrjuntos.project.race;

import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.correrjuntos.project.JWT.Jwt;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class RaceController {
    
    private RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    // INSERT RACE //

    @PostMapping(value="/api/race/insert")
    Map<String, Object> insertRace(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> body) {

        Map<String, Object> checkJWT = Jwt.checkJWT((String)headers.get("authorization") , (String)body.get("userMail"));

        Map<String, Boolean> resultCheckJwt = raceService.jwtResponse(checkJWT);

        Map<String, Object> getBodyResponse = new HashMap<String, Object>();

        if (resultCheckJwt.get("tokenExpires") == false) {

            int maxUsers = 0;
            int numberStreet = 0;

            try {
                maxUsers = Integer.parseInt((String)body.get("inputMaxUsers"));
                numberStreet = Integer.valueOf((String)body.get("inputStreetNumber"));
            }

            catch (NumberFormatException e) {
                maxUsers = -1;
                numberStreet = -1;
            }

            // convert to time to insert to database //

            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime time = LocalTime.parse((String)body.get("selectHour"), formatterTime);

            //

            Race lastInsertedRaceByUser = raceService.insertRace(
                (Boolean)body.get("inputCheckBoxNoLimit"),
                StringEscapeUtils.escapeHtml4((String)body.get("inputCity")),
                (String)body.get("inputDate"),
                maxUsers,
                (String)body.get("inputRaceTime"),
                StringEscapeUtils.escapeHtml4((String)body.get("inputStreetName")),
                numberStreet,
                time,
                (String)body.get("selectLevel"),
                StringEscapeUtils.escapeHtml4((String)body.get("textAreaFurtherDetails")),
                (String)body.get("userMail"),
                (Integer)body.get("userId")
            );

            raceService.insertRaceUser(
                lastInsertedRaceByUser.getId(),
                (Integer)body.get("userId")
            );

            getBodyResponse.put("bodyResponse", true);

        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            getBodyResponse.put("bodyResponse", false);
        }

        return getBodyResponse;

    }

    // UPDATE RACE //

    @PutMapping(value="/api/race/update/{race_id}")
    Map<String, Object> updateRace(
        @RequestBody Map<String, Object> body,
        @RequestHeader Map<String, String> headers,
        @PathVariable Long race_id
    ) {

        Map<String, Object> checkJWT = Jwt.checkJWT((String)headers.get("authorization") , (String)body.get("userMail"));

        Map<String, Boolean> resultCheckJwt = raceService.jwtResponse(checkJWT);

        Map<String, Object> getBodyResponse = new HashMap<String, Object>();

        if (resultCheckJwt.get("tokenExpires") == false) {

            Boolean inputCheckBoxNoLimit = (Boolean)body.get("inputCheckBoxNoLimit");

            int maxUsers = 0;
            int numberStreet = 0;

            try {

                maxUsers = Integer.parseInt((String)body.get("inputMaxUsers"));

                numberStreet = Integer.valueOf((String)body.get("inputStreetNumber"));
            }

            catch (NumberFormatException e) {
                maxUsers = -1;
                numberStreet = -1;
            }

            if (inputCheckBoxNoLimit == false) {

                maxUsers = Integer.parseInt((String)body.get("inputMaxUsers"));
            }

            else if (inputCheckBoxNoLimit == true) {

                maxUsers= -1;
            }

            // convert to time to insert to database //

            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime time = LocalTime.parse((String)body.get("selectHour"), formatterTime);

            //

            String inputCity = StringEscapeUtils.escapeHtml4((String)body.get("inputCity"));
            String inputDate = (String)body.get("inputDate");

            String inputRaceTime = (String)body.get("inputRaceTime");
            String inputStreetName = StringEscapeUtils.escapeHtml4((String)body.get("inputStreetName"));

            String selectLevel = (String)body.get("selectLevel");

            String textAreaFurtherDetails = StringEscapeUtils.escapeHtml4((String)body.get("textAreaFurtherDetails"));
            String userMail = (String)body.get("userMail");
            Integer userId = (Integer)body.get("userId");

            raceService.updateRace(
                inputCity,
                inputDate,
                maxUsers,
                inputRaceTime,
                inputStreetName,
                numberStreet,
                time,
                selectLevel,
                textAreaFurtherDetails,
                race_id
            );

            getBodyResponse.put("bodyResponse", true);

            return getBodyResponse;
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            getBodyResponse.put("bodyResponse", false);

            return getBodyResponse;
        }

        return getBodyResponse;
    }

    // GET LAST RACES //

    @GetMapping(value="/api/race/getlastraces")
    Map<String, Map<String, Object>> getLastRaces() {

        // fetch last races //
        
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();

        Map<String, Map<String, Object>> getLastRaces = raceService.getLastRaces();

        Map<String, Map<String, Object>> newMapObj = new HashMap<String, Map<String, Object>>();

        Object[] keys = getLastRaces.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {

            Map<String, Object> newObjEscaped = new HashMap<String, Object>();

            for (String str : getLastRaces.get(keys[i]).keySet()) {
                
                if (getLastRaces.get(keys[i]).get(str).getClass() == String.class) {
                    newObjEscaped.put(str, StringEscapeUtils.unescapeHtml4(getLastRaces.get(keys[i]).get(str).toString()));
                }

                else {
                    newObjEscaped.put(str, getLastRaces.get(keys[i]).get(str));
                }
            }

            newMapObj.put(String.valueOf(i), newObjEscaped);
        }

        Map<String, Object> resultLastRaces = new HashMap<String, Object>();
        resultLastRaces.put("lastRaces", newMapObj);

        result.put("fetchLastRaces", resultLastRaces);

        // get users participate race //

        List<Map<String, Object>> getCountUsersParticipate = raceService.getCountUsersParticipate();

        Map<String, Object> getCountUsersParticipateResult = new HashMap<String, Object>();

        for (int i = 0; i < getCountUsersParticipate.size(); i++) {

            getCountUsersParticipateResult.put(String.valueOf(i), getCountUsersParticipate.get(i));
        }


        Map<String, Object> getCount = new HashMap<String, Object>();
        getCount.put("getCountUsersParticipate", getCountUsersParticipate);

        result.put("fetchCountUsersParticipate", getCountUsersParticipateResult);

        return result;
    }

    // GET RACE BY ID //

    @GetMapping(value="/api/race/findrace/{race_id}")
    Map<String, Object> findRaceById(@PathVariable("race_id") Long race_id) {

        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<Object, Object>> getResult = raceService.findRaceById(race_id);
        Map<String, Map<Object, Object>> getUsersParticipate = raceService.getUsersParticipate(race_id);

        // create new object with unescaped results //

        List<Map<Object, Object>> newList = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> getResultUnEscaped = new HashMap<Object, Object>();

        for (Object key : getResult.get(0).keySet()) {

            if (getResult.get(0).get(key).getClass() == String.class) {

                getResultUnEscaped.put(key.toString(), StringEscapeUtils.unescapeHtml4(getResult.get(0).get(key).toString()));
            }

            else {
                
                if (getResult.get(0).get(key).getClass() == Timestamp.class) {

                    getResultUnEscaped.put(key, getResult.get(0).get(key).toString().substring(0, getResult.get(0).get(key).toString().indexOf(".")));
                }

                else {

                    getResultUnEscaped.put(key.toString(), getResult.get(0).get(key));
                }
            }
        }

        newList.add(getResultUnEscaped);

        result.put("race_info", /*getResult.get(0)*/newList.get(0));
        result.put("users_participate", getUsersParticipate);

        return result;
    }

    // PARTICIPATE RACE //

    @PostMapping(value="/api/race/participate/{race_id}")
    Map<String, Map<Object, Object>> participateRace(
        @RequestBody Map<String, Object> body,
        @RequestHeader Map<String, String> headers,
        @PathVariable("race_id") Long race_id) {

        Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), (String)body.get("user_mail"));

        Map<String, Boolean> resultCheckJwt = raceService.jwtResponse(checkJWT);

        if (resultCheckJwt.get("tokenExpires") == false) {

            List<Map<Object, Object>> getRaceById = raceService.findRaceById(race_id);
            Map<String, Map<Object, Object>> getUsersParticipate = raceService.getUsersParticipate(race_id);

            // check if no limit users //

            Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();
            Map<Object, Object> response = new HashMap<Object, Object>();

            Integer usersMax = (Integer)getRaceById.get(0).get("number_users");

            if (usersMax == -1) {

                Integer userId = (Integer)body.get("user_id");

                raceService.insertRaceUser(
                    race_id,
                    userId
                );

                response.put("success", "User successfully registered for this race.");
                result.put("response", response);

                return result;
            }

            //

            if ((Integer)getUsersParticipate.keySet().size() < (Integer)getRaceById.get(0).get("number_users")) {

                Boolean flag = false;

                for (String key : getUsersParticipate.keySet()) {

                    for (Object keyEachObj : getUsersParticipate.get(key).keySet()) {

                        if (keyEachObj.toString().equals("user_id")) {
                            
                            Long userId = (Long)getUsersParticipate.get(key).get(keyEachObj);

                            if ((Integer)userId.intValue() == body.get("user_id")) {

                                flag = true;
                            }
                        }
                    }
                }

                if (flag) {

                    response.put("error", "User already registered for this race.");
                    result.put("response", response);

                    return result;
                }

                else if (!flag) {

                    Integer userId = (Integer)body.get("user_id");

                    raceService.insertRaceUser(
                        race_id,
                        userId
                    );

                    response.put("success", "User successfully registered for this race.");
                    result.put("response", response);

                    return result;
                }

            }

            else {

                response.put("error", "User limit reached for this race.");
                result.put("response", response);

                return result;
            }

        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();
            Map<Object, Object> response = new HashMap<Object, Object>();

            response.put("error", "Authentication error.");
            result.put("response", response);

            return result;
        }

        Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();

        return result;
    }

    // DELETE USER PARTICIPATE RACE //

    @DeleteMapping(value="/api/race/participate/delete/user/{user_id}/email/{email}/race/{race_id}")
    Map<String, Map<Object, Object>> deleteUserRace(
        @RequestHeader Map<String, String> headers,
        @PathVariable Long user_id,
        @PathVariable String email,
        @PathVariable Long race_id
    ) {

        Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), email);
        Map<String, Boolean> resultCheckJwt = raceService.jwtResponse(checkJWT);

        Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();
        Map<Object, Object> response = new HashMap<Object, Object>();

        if (resultCheckJwt.get("tokenExpires") == false) {

            Integer userId = (Integer)user_id.intValue();

            raceService.deleteRaceUser(race_id, userId);

            response.put("success", "Race participation canceled.");
            result.put("response", response);
            return result;
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            response.put("error", "Authentication error.");
            result.put("response", response);
            return result;
        }

        return result;
    }

    // DELETE RACE //

    @DeleteMapping(value="/api/race/delete/{race_id}/user/{email}")
    Map<String, Map<Object, Object>> deleteRace(
        @RequestHeader Map<String, String> headers,
        @PathVariable Long race_id,
        @PathVariable String email
    ) {

        Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), email);
        Map<String, Boolean> resultCheckJwt = raceService.jwtResponse(checkJWT);

        Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();
        Map<Object, Object> response = new HashMap<Object, Object>();

        if (resultCheckJwt.get("tokenExpires") == false) {

            raceService.deleteRace(race_id);

            response.put("success", "Race deleted successfully.");
            result.put("response", response);
            return result;
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            response.put("error", "Authentication error.");
            result.put("response", response);
            return result;
        }

        return result;
    }

    @GetMapping(value="/api/race/getracesbyuser/{email}/{user_id}")
    Map<String, Map<String, Object>> getRacesByUser(
        @RequestHeader Map<String, String> headers,
        @PathVariable(value = "email") String email,
        @PathVariable(value = "user_id") Long user_id
    ) {

        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        Map<String, Object> response = new HashMap<String, Object>();

        Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), email);
        Map<String, Boolean> resultCheckJwt = raceService.jwtResponse(checkJWT);

        if (resultCheckJwt.get("tokenExpires") == false) {

            return raceService.findAllRacesByUser(user_id);
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            response.put("error", "Authentication error.");
            result.put("response", response);
            return result;
        }

        return result;
    }
}
