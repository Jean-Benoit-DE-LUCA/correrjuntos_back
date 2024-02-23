package com.correrjuntos.project.contact;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.text.StringEscapeUtils;

import javax.mail.*;
import javax.mail.internet.*;

@CrossOrigin
@RestController
public class ContactController {

    public ContactController() {

    }
    
    @PostMapping(value="/api/contact/send")
    Map<String, Object> contactSend(
        @RequestBody Map<String, Object> body
    ) {

        Map<String, Object> result = new HashMap<String, Object>();

        String firstName = (String)body.get("firstName");
        String lastName = (String)body.get("lastName");
        String email = (String)body.get("email");
        String message = (String)body.get("message");


        if (firstName.length() != 0 &&
            lastName.length() != 0 &&
            email.length() != 0 &&
            message.length() != 0) {

            
            Properties env = new Properties();
            
            try {
                FileReader envProperties = new FileReader("env.properties");
                env.load(envProperties);

                String mailContact = env.getProperty("MAIL_CONTACT");
                String passwordMailContact = env.getProperty("MAIL_PASSWORD");


                String from = email;
                String password = passwordMailContact;
                String to = mailContact;


                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(properties, new Authenticator() {
                    
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(mailContact, password);
                    }
                });

                try {

                    MimeMessage mess = new MimeMessage(session);
                    mess.setFrom(new InternetAddress(from, "Correrjuntos"));
                    mess.setReplyTo(new Address[] {
                        new InternetAddress(from)
                    });
                    mess.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    mess.setSubject("Correrjuntos contact message");
                    mess.setText(StringEscapeUtils.escapeHtml4(message));

                    Transport.send(mess);

                    result.put("success", true);

                    return result;
                }

                catch (MessagingException e) {

                    result.put("error", e.getMessage());

                    return result;
                }
            }

            catch (FileNotFoundException e) {

                result.put("error", e.getMessage());

                return result;
            }

            catch (IOException e) {

                result.put("error", e.getMessage());

                return result;
            }
        }

        return result;
    }
}
