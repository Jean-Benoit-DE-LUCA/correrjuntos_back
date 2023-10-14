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
        String streetNumber,
        String streetName,
        String zipCode,
        String city,
        String password
        ) 
    {

        StrongPasswordEncryptor encrypt = new StrongPasswordEncryptor();
        String passwordHashed = encrypt.encryptPassword(password);

        userRepository.insertUser(
            firstName,
            lastName,
            email,
            LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-d")),
            Integer.parseInt(streetNumber),
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
}
