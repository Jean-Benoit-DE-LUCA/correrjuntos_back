package com.correrjuntos.project.user;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.web.bind.annotation.CrossOrigin;
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
            body.containsKey("streetNumber") &&
            body.containsKey("streetName") &&
            body.containsKey("zipCode") &&
            body.containsKey("city") &&
            body.containsKey("password") &&
            body.containsKey("passwordConfirm")
            ) 
        {   
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
}
