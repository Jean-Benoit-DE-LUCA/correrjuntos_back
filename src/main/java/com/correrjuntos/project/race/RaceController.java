package com.correrjuntos.project.race;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.correrjuntos.project.JWT.Jwt;
import java.lang.reflect.Field;

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

            try {
                maxUsers = Integer.parseInt((String)body.get("inputMaxUsers"));
            }

            catch (NumberFormatException e) {
                maxUsers = -1;
            }

            Race lastInsertedRaceByUser = raceService.insertRace(
                (Boolean)body.get("inputCheckBoxNoLimit"),
                (String)body.get("inputCity"),
                (String)body.get("inputDate"),
                maxUsers,
                (String)body.get("inputRaceTime"),
                (String)body.get("inputStreetName"),
                Integer.valueOf((String)body.get("inputStreetNumber")),
                (String)body.get("selectHour"),
                (String)body.get("textAreaFurtherDetails"),
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

    // GET LAST RACES //

    @GetMapping(value="/api/race/getlastraces")
    Map<String, Object> getLastRaces() {
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("lastRaces", raceService.getLastRaces());

        return result;
    }

    // GET RACE BY ID //

    @GetMapping(value="/api/race/findrace/{race_id}")
    Map<String, Object> findRaceById(@PathVariable("race_id") Long race_id) {

        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<Object, Object>> getResult = raceService.findRaceById(race_id);
        Map<String, Map<Object, Object>> getUsersParticipate = raceService.getUsersParticipate(race_id);

        result.put("race_info", getResult.get(0));
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

            if ((Integer)getUsersParticipate.keySet().size() < (Integer)getRaceById.get(0).get("number_users")) {

                Boolean flag = false;

                Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();

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

                Map<Object, Object> response = new HashMap<Object, Object>();

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

                Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();
                Map<Object, Object> response = new HashMap<Object, Object>();

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
}
