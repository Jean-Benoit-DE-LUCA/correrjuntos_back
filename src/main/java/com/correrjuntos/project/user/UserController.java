package com.correrjuntos.project.user;

import java.util.Map;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.correrjuntos.project.JWT.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value="/api/user/register")
    public Map<String, Object> insertUser(@RequestBody Map<String, Object> body, HttpServletRequest request, HttpServletResponse response) {

        if (body.containsKey("firstName") &&
            body.containsKey("lastName") &&
            body.containsKey("email") &&
            body.containsKey("birthDate") &&
            body.containsKey("gender") &&
            body.containsKey("streetNumber") &&
            body.containsKey("streetName") &&
            body.containsKey("zipCode") &&
            body.containsKey("city") &&
            body.containsKey("password") &&
            body.containsKey("passwordConfirm")
            ) 
        {   

            if (body.get("firstName").toString().contains("<") || body.get("firstName").toString().contains(">") || body.get("firstName").toString().contains("\"") || body.get("firstName").toString().contains("'") || body.get("firstName").toString().contains("&") || body.get("lastName").toString().contains("<") || body.get("lastName").toString().contains(">") || body.get("lastName").toString().contains("\"") || body.get("lastName").toString().contains("'") || body.get("lastName").toString().contains("&") || body.get("email").toString().contains("<") || body.get("email").toString().contains(">") || body.get("email").toString().contains("\"") || body.get("email").toString().contains("'") || body.get("email").toString().contains("&") || body.get("streetName").toString().contains("<") || body.get("streetName").toString().contains(">") || body.get("streetName").toString().contains("\"") || body.get("streetName").toString().contains("'") || body.get("streetName").toString().contains("&") || body.get("city").toString().contains("<") || body.get("city").toString().contains(">") || body.get("city").toString().contains("\"") || body.get("city").toString().contains("\"") || body.get("city").toString().contains("'") || body.get("city").toString().contains("&") || body.get("password").toString().contains("<") || body.get("password").toString().contains(">") || body.get("password").toString().contains("\"") || body.get("password").toString().contains("'") || body.get("password").toString().contains("&")){

                Map<String, Object> result = new HashMap<String, Object>();
                result.put("error", "Invalid character.");
                return result;
            }


            String password = (String)body.get("password");
            String passwordConfirm = (String)body.get("passwordConfirm");
            
            boolean passEqual = password.equals(passwordConfirm);

            if (passEqual) {

                List<User> findUser = userService.findUserByMail((String)body.get("email"));

                if (findUser.size() == 0) {

                    userService.insertUser(
                    (String)body.get("firstName"),
                    (String)body.get("lastName"),
                    (String)body.get("email"),
                    (String)body.get("birthDate"),
                    (String)body.get("gender"),
                    (String)body.get("streetNumber"),
                    (String)body.get("streetName"),
                    (String)body.get("zipCode"),
                    (String)body.get("city"),
                    (String)body.get("password")
                    );

                    List<User> findInsertedUser = userService.findUserByMail((String)body.get("email"));
                    
                    Map<String, String> token = Jwt.setJWT((String)body.get("email"));
                    Map<String, Object> result = new HashMap<String, Object>();

                    result.put("user", findInsertedUser.get(0));
                    result.put("token", token.get("token"));
                    return result;
                }

                else {

                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("error", "Email already registered.");
                    return result;
                }

            }

            else {

                Map<String, Object> result = new HashMap<String, Object>();
                result.put("error", "The passwords don't match.");
                return result;
            }

        }

        else {

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("error", "Missing properties.");
            return result;
        }

    }

    @PostMapping(value="/api/user/signin")
    public Map<String, Object> signInUser(@RequestBody Map<String, Object> body) {

        if (body.get("password").toString().length() != 0 || body.get("email").toString().length() != 0) {

            if (body.get("email").toString().contains("<") || body.get("email").toString().contains(">") || body.get("email").toString().contains("\"") || body.get("email").toString().contains("'") || body.get("email").toString().contains("&") || body.get("password").toString().contains("<") || body.get("password").toString().contains(">") || body.get("password").toString().contains("\"") || body.get("password").toString().contains("'") || body.get("password").toString().contains("&")) {

                Map<String, Object> result = new HashMap<String, Object>();
                    result.put("error", "Invalid character.");
                    result.put("flag", false);
                    return result;
            }

            List<User> findUserByMail = userService.findUserByMail((String)body.get("email"));

            if (findUserByMail.size() > 0) {

                StrongPasswordEncryptor encrypt = new StrongPasswordEncryptor();
                boolean passwordMatch = encrypt.checkPassword(body.get("password").toString(), findUserByMail.get(0).getPassword());

                if (passwordMatch) {

                    Map<String, String> token = Jwt.setJWT((String)body.get("email"));
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("result", findUserByMail.get(0));
                    result.put("flag", true);
                    result.put("token", token.get("token"));
                    return result;
                }

                else {

                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("error", "The passwords don't match.");
                    result.put("flag", false);
                    return result;
                }
            }

            else {

                Map<String, Object> result = new HashMap<String, Object>();
                result.put("error", "User not found.");
                result.put("flag", false);
                return result;
            }
        }

        else {

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("error", "Empty fields.");
            result.put("flag", false);
            return result;

        }
    }

    @GetMapping(value="/api/user/get/{user_id}")
    public Map<String, Object> findUserById(
        @PathVariable Integer user_id
    ) throws IllegalArgumentException, IllegalAccessException {

        Map<String, Object> result = new HashMap<String, Object>();

        List<User> fetchUser = userService.findUserById(user_id);

        Field[] fields = fetchUser.get(0).getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);
            if (field.getName().equals("birthDate")) {
                
                LocalDate now = LocalDate.now();
                Long years = ChronoUnit.YEARS.between((LocalDate)field.get(fetchUser.get(0)), now);
                result.put("age", years);
            }

            else {

                result.put(field.getName(), field.get(fetchUser.get(0)));
            }
            
        }

        return result;
    }
}
