package com.correrjuntos.project.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.correrjuntos.project.JWT.Jwt;

@CrossOrigin
@RestController
public class ReviewController {
    
    private ReviewService reviewService;

    public ReviewController (ReviewService reviewService) {

        this.reviewService = reviewService;
    }

    @GetMapping(value="/api/review/get/user_id/{user_id}")
    public Map<String, Map<String, Object>> getReview(
        @PathVariable("user_id") Integer user_id
    ) throws IllegalArgumentException, IllegalAccessException {


        Map<String, Map<String, Object>> getReviewsByUserId = reviewService.getReview(
            user_id
        );

        return getReviewsByUserId;
    }



    @PostMapping(value="/api/review/insert")
    public Map<String, Object> insertReview(
        @RequestHeader Map<String, String> headers,
        @RequestBody Map<String, Object> body
    ) {

        String email = (String)body.get("email");

        Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), email);
        Map<String, Boolean> resultCheckJwt = reviewService.jwtResponse(checkJWT);

        Map<String, Object> result = new HashMap<String, Object>();

        if (resultCheckJwt.get("tokenExpires") == false) {

            String comment = StringEscapeUtils.escapeHtml4((String)body.get("comment"));
            Integer rate = Integer.valueOf((String)body.get("rate"));
            Integer user_id = (Integer)body.get("user_id");
            Integer user_reviewed = (Integer)body.get("user_reviewed");


            if (user_id != user_reviewed) {

                if (reviewService.checkReview(
                    user_id,
                    user_reviewed
                ).size() == 0) {

                    reviewService.insertReview(
                        comment,
                        rate,
                        user_id,
                        user_reviewed
                    );
                
                    result.put("flag", true);

                    return result;
                }

                else {

                    result.put("checkReview", false);

                    return result;
                }

            }

            else {

                result.put("checkUser", false);

                return result;
            }
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            result.put("flag", false);

            return result;
        }

        return result;
    }



    // FETCH REVIEWS BY USER ID //

    @GetMapping(value="/api/review/fetch/user_id/{user_id}")
    Map<String, Map<String, Object>> fetchReviewsByUserId(
        @PathVariable("user_id") Integer user_id
    )

    {

        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        Map<String, Object> obj = new HashMap<String, Object>();

        Map<String, Map<String, Object>> fetchReviews = reviewService.fetchReviews(user_id);

        return fetchReviews;
    }




    // DELETE REVIEW //

    @DeleteMapping(value="/api/review/delete/{review_id}/email/{email}")
    public Map<String, Object> deleteReview(
        @RequestHeader Map<String, String> headers,
        @PathVariable("review_id") Integer review_id,
        @PathVariable("email") String email
    )

    {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), email);
        Map<String, Boolean> resultCheckJwt = reviewService.jwtResponse(checkJWT);

        if (resultCheckJwt.get("tokenExpires") == false) {

            reviewService.deleteReview(review_id);

            result.put("flag", true);

            return result;
        }

        else if (resultCheckJwt.get("tokenExpires") == true) {

            result.put("flag", false);

            return result;
        }

        return result;
    }
}
