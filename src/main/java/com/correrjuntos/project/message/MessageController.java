package com.correrjuntos.project.message;

import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.correrjuntos.project.JWT.Jwt;


@RestController
@CrossOrigin
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {

        this.messageService = messageService;
    }

    @GetMapping(value="/api/message/get/race_id/{race_id}")
    public Map<String, Object> getMessagesByRace(
        @PathVariable Integer race_id
    ) {

        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> listMessages = messageService.getMessagesByRace(race_id);

        Map<String, Map<String, Object>> listMessagesJson = new HashMap<String, Map<String, Object>>();

        for (int i = 0; i < listMessages.size(); i++) {

            Map<String, Object> newMap = new HashMap<String, Object>();

            for (String key : listMessages.get(i).keySet()) {
                
                if (key.equals("message")) {
                    newMap.put(key, StringEscapeUtils.unescapeHtml4((String)listMessages.get(i).get(key)));
                }

                else {
                    newMap.put(key, listMessages.get(i).get(key));
                }

            }

            listMessagesJson.put(String.valueOf(i), newMap);
        }

        result.put("result", listMessagesJson);

        return result;
    }

    @PostMapping(value="/api/message/insert")
    public Map<String, Object> postMessage(
        @RequestBody Map<String, Object> body,
        @RequestHeader Map<String, String> headers
    ) {

        Map<String, Object> checkJWT = Jwt.checkJWT((String)headers.get("authorization") , (String)body.get("email"));

        Map<String, Boolean> resultCheckJwt = messageService.jwtResponse(checkJWT);

        Map<String, Object> result = new HashMap<String, Object>();

        if (resultCheckJwt.get("tokenExpires") == false) {

            String message = StringEscapeUtils.escapeHtml4((String)body.get("inputMessage"));
            Integer race_id = (Integer)body.get("raceId");
            Integer user_id = (Integer)body.get("userId");



            // check if race exists //

            List<Map<String, Object>> getRaceId = messageService.getRaceId(race_id);

            if (getRaceId.size() == 0) {

                result.put("error", "Race does not exist.");

                return result;
            }






            // else insert message //

            String dateTimeBody = (String)body.get("currentDateTime");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeBody, formatter);

            messageService.insertMessage(
                message,
                race_id,
                user_id,
                dateTime,
                dateTime
            );

            result.put("flag", true);

        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            result.put("flag", false);

        }

        return result;
    }
}
