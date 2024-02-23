package com.correrjuntos.project.user;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public void insertUser(
        String firstName,
        String lastName,
        String email,
        String birthDate,
        String gender,
        String streetNumber,
        String streetName,
        String zipCode,
        String city,
        String password
        ) 
    {

        StrongPasswordEncryptor encrypt = new StrongPasswordEncryptor();
        String passwordHashed = encrypt.encryptPassword(password);

        Integer streetNumberFormat;

        try {
            streetNumberFormat = Integer.parseInt(streetNumber);
        }

        catch (Exception e) {
            streetNumberFormat = -1;
        }

        userRepository.insertUser(
            firstName,
            lastName,
            email,
            LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-d")),
            gender,
            streetNumberFormat,
            streetName,
            Integer.parseInt(zipCode),
            city,
            passwordHashed
        );
    }


    public List<User> findUserByMail(
        String email
    )

    {
        return userRepository.findUserByMail(
            email
        );
    }

    public List<Map<String, Object>> findUserById(
        Integer user_id
    )

    {

        return userRepository.findUserById(
            user_id
        );
    }

    public void update(
        String firstName,
        String lastName,
        String email,
        String birthDate,
        String gender,
        Integer streetNumber,
        String streetName,
        Integer zipCode,
        String city,
        String password,
        String fileName,
        Integer user_id
    )

    {

        StrongPasswordEncryptor encrypt = new StrongPasswordEncryptor();
        String passwordHashed = encrypt.encryptPassword(password);

        userRepository.update(
            firstName,
            lastName,
            email,
            LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-d")),
            gender,
            streetNumber,
            streetName,
            zipCode,
            city,
            passwordHashed,
            fileName,
            user_id
        );
    }
}
