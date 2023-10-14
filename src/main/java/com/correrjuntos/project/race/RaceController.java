package com.correrjuntos.project.race;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.correrjuntos.project.JWT.Jwt;

@RestController
@CrossOrigin
public class RaceController {
    
    private RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

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

            raceService.insertRace(
                (Boolean)body.get("inputCheckBoxNoLimit"),
                (String)body.get("inputCity"),
                (String)body.get("inputDate"),
                maxUsers,
                (String)body.get("inputRaceTime"),
                (String)body.get("inputStreetName"),
                (String)body.get("inputStreetNumber"),
                (String)body.get("selectHour"),
                (String)body.get("textAreaFurtherDetails"),
                (String)body.get("userMail"),
                (Integer)body.get("userId")
            );

            getBodyResponse.put("bodyResponse", true);
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            getBodyResponse.put("bodyResponse", false);
        }

        return getBodyResponse;
    }
    
}
