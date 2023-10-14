package com.correrjuntos.project.JWT;

import java.io.FileInputStream;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Jwt {
    
    public static Map<String, String> setJWT(String issuer) {

        try {

        Properties props = new Properties();
        props.load(new FileInputStream("env.properties"));

        String token = JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(Date.from(ZonedDateTime.now().plusSeconds(1800).toInstant()))
            .sign(Algorithm.HMAC256(props.getProperty("SECRET_KEY_JWT")));

        Map<String, String> tokenObj = new HashMap<String, String>();
        tokenObj.put("token", token);

        return tokenObj;

        }

        catch(JWTCreationException e) {

            Map<String, String> errorObj = new HashMap<String, String>();
            errorObj.put("token", e.getMessage());

            return errorObj;
        }

        catch (Exception e) {

            try {

                throw new Exception();
            }

            catch (Exception err) {

                Map<String, String> errorObj = new HashMap<String, String>();
                errorObj.put("token", e.getMessage());

                return errorObj;
            }
        }

    }

    public static Map<String, Object> checkJWT(String token, String mailIssuer) {

        DecodedJWT decodedJwt;

        try {

            Properties props = new Properties();
            props.load(new FileInputStream("env.properties"));

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(props.getProperty("SECRET_KEY_JWT")))
                .withIssuer(mailIssuer)
                .build();

            decodedJwt = verifier.verify(token);

            Map<String, Object> tokenObj = new HashMap<String, Object>();
            tokenObj.put("tokenCheckResult", decodedJwt);

            return tokenObj;
        }

        catch (JWTVerificationException e) {

            Map<String, Object> tokenObj = new HashMap<String, Object>();
            tokenObj.put("tokenCheckResult", e.getMessage());

            return tokenObj;
        }

        catch (Exception e) {

            try {
                throw new Exception();
            }

            catch (Exception err) {

                Map<String, Object> tokenObj = new HashMap<String, Object>();
                tokenObj.put("tokenCheckResult", e.getMessage());

                return tokenObj;
            }
        }
    }
}
