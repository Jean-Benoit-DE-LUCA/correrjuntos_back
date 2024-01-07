package com.correrjuntos.project.user;

import java.util.Map;

import javax.naming.SizeLimitExceededException;
import javax.print.attribute.standard.Media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.correrjuntos.project.JWT.Jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@CrossOrigin
@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // INSERT USER //

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

    // SIGN IN USER //

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

    @GetMapping(value="/api/user/get/id/{user_id}")
    public Map<String, Object> findUserById(
        //@RequestHeader Map<String, String> headers,
        @PathVariable Integer user_id
        //@PathVariable String email
    ) throws IllegalArgumentException, IllegalAccessException {

       /*Map<String, Object> checkJWT = Jwt.checkJWT(headers.get("authorization"), email);
        Map<String, Boolean> resultCheckJwt = userService.jwtResponse(checkJWT);*/

        Map<String, Object> result = new HashMap<String, Object>();

        //if (resultCheckJwt.get("tokenExpires") == false) {

            List<Map<String, Object>> fetchUser = userService.findUserById(user_id);

            for (String key : fetchUser.get(0).keySet()) {

                if (key.equals("birth_date")) {

                    LocalDate now = LocalDate.now();
                    Long years = ChronoUnit.YEARS.between(LocalDate.parse(fetchUser.get(0).get(key).toString()), now);
                    result.put("age", years);
                }

                else {

                    result.put(key, fetchUser.get(0).get(key));
                }
            }

            return result;

        /*}

        else if (resultCheckJwt.get("tokenExpires") == true) {

            result.put("error", "Authentication error.");
        }*/

        //return result;
    }

    // PROFILE USER //

    @PostMapping(value="/api/user/profile/{user_id}", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public Map<String, Object> update(
        HttpServletRequest request,
        @PathVariable("user_id") Integer user_id
    ) throws IOException, ServletException {

        // get user data //

        List<Map<String, Object>> userObj = userService.findUserById(user_id);



        // get path uploads //

        Path currentPath = Paths.get("");
        String pathUpload = currentPath.toAbsolutePath() + "/src/main/resources/static/uploads/";



        // check if "noImage" key is set //

        String noImage = null;

        try {

            noImage = request.getParameter("noImage");
        }

        catch (Exception e) {

            noImage = null;
        }



         // get file and upload //

        String fileName = null;

        try {

            Part filePart = request.getPart("file");
            fileName = filePart.getSubmittedFileName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));


            // check file extension //

            Boolean checkExtension = false;

            String[] acceptedExtensions = {".jpg", ".jpeg", ".gif", ".png", ".svg", ".bmp"};

            for (String extension : acceptedExtensions) {

                if (extension.equals(fileExtension)) {

                    checkExtension = true;
                    break;
                }
            }



            if (!checkExtension) {

                Map<String, Object> result = new HashMap<String, Object>();

                result.put("flag", false);
                result.put("error", "File extension not accepted");

                return result;
            }





            // check file size //

            long fileSize = filePart.getSize();

            if (fileSize > 5242880) {

                Map<String, Object> result = new HashMap<String, Object>();

                result.put("flag", false);
                result.put("error", "Max upload size exceed");

                return result;
            }


            

            // delete file before insert //

            File fileToDelete = new File(pathUpload + userObj.get(0).get("picture"));
            fileToDelete.delete();

            //

            InputStream fileContent = filePart.getInputStream();
            File fileToSave = new File(pathUpload + fileName);
            Files.copy(fileContent, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        catch (NullPointerException e) {

            if (noImage != null) {

                if (userObj.get(0).get("picture") == null) {

                    fileName = null;
                }

                else if (userObj.get(0).get("picture") != null) {

                    File fileToDelete = new File(pathUpload + userObj.get(0).get("picture"));
                    fileToDelete.delete();

                    fileName = null;
                }

                else {

                    fileName = null;
                }
            }

            else if (noImage == null) {

                fileName = userObj.get(0).get("picture").toString();
            }
            
        }

        catch (Exception e) {

            fileName = userObj.get(0).get("picture").toString();
        }


        //


        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String birthDate = request.getParameter("birthDate");
        String gender = request.getParameter("gender");


        // get street number //

        Integer streetNumber = -1;

        try {
            streetNumber = Integer.valueOf(request.getParameter("streetNumber"));
        }
        catch (Exception error) {
            streetNumber = -1;
        }

        //


        String streetName = request.getParameter("streetName");


        // get zip code //

        Integer zipCode = 0;

        try {
            zipCode = Integer.valueOf(request.getParameter("zipCode"));
        }
        catch (Exception error) {
            zipCode = 00000;
        }

        //


        String city = request.getParameter("city");
        String password = request.getParameter("password");


        // CALL SERVICE METHOD //

        userService.update(
            firstName,
            lastName,
            email,
            birthDate,
            gender,
            streetNumber,
            streetName,
            zipCode,
            city,
            password,
            fileName,
            user_id
        );

        //



        // return update user object //

        List<Map<String, Object>> updatedUserObj = userService.findUserById(user_id);


        Map<String, Object> result = new HashMap<String, Object>();

        result.put("flag", true);
        result.put("userObj", updatedUserObj.get(0));

        return result;
    }
}
