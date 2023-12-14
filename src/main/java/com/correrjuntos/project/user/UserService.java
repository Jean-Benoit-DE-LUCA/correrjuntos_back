package com.correrjuntos.project.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public List<User> findUserById(
        Integer user_id
    )

    {

        return userRepository.findUserById(
            user_id
        );
    }
}
