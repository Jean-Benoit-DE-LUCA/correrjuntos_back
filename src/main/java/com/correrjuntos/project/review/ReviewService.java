package com.correrjuntos.project.review;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    
    private ReviewRepository reviewRepository;

    public ReviewService (ReviewRepository reviewRepository) {

        this.reviewRepository = reviewRepository;
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

    // CHECK REVIEW //

    public List<Map<String, Object>> checkReview(
        Integer user_id,
        Integer user_reviewed
    ) {

        return reviewRepository.checkReview(
            user_id,
            user_reviewed
        );
    }

    // GET REVIEW BY USER ID //

    public Map<String, Map<String, Object>> getReview(
        Integer user_id
    ) throws IllegalArgumentException, IllegalAccessException {

        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();

        for (int i = 0; i < reviewRepository.getReview(user_id).size(); i++) {

            Map<String, Object> reviewObj = new HashMap<String, Object>();

            for (String key : reviewRepository.getReview(user_id).get(i).keySet()) {

                if (reviewRepository.getReview(user_id).get(i).get(key).getClass().getSimpleName().equals("String")) {

                    reviewObj.put(key, StringEscapeUtils.unescapeHtml4((String)reviewRepository.getReview(user_id).get(i).get(key)));
                }

                else {

                    reviewObj.put(key, reviewRepository.getReview(user_id).get(i).get(key));
                }
            }

            result.put(String.valueOf(i), reviewObj);
        }

        return result;
    }



    // INSERT REVIEW //

    public void insertReview(
        String comment,
        Integer rate,
        Integer user_id,
        Integer user_reviewed
    ) {

        ZonedDateTime dateNow = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime created_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);
        LocalDateTime updated_at = LocalDateTime.parse(dateNow.toString().replace("T", " ").substring(0, dateNow.toString().replace("T", " ").indexOf(".")), formatter);

        reviewRepository.insertReview(
            comment,
            rate,
            user_id,
            user_reviewed,
            created_at,
            updated_at
        );
    }
}
