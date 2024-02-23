package com.correrjuntos.project.message;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {

        this.messageRepository = messageRepository;
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

    public List<Map<String, Object>> getMessagesByRace(
        Integer race_id
    ) {

        return messageRepository.getMessagesByRace(race_id);
    }

    public void insertMessage(
        String message,
        Integer race_id,
        Integer user_id,
        LocalDateTime created_at,
        LocalDateTime updated_at
    ) {

        messageRepository.insertMessage(
            message,
            race_id,
            user_id,
            created_at,
            updated_at
        );

    }
}
