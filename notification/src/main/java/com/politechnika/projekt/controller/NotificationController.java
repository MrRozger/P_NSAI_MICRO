package com.politechnika.projekt.controller;

import com.politechnika.projekt.model.Email;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

@RestController
public class NotificationController {

    @Value("${gmail.username}")
    private String username;

    @Value("${gmail.password}")
    private String password;

    @RabbitListener(queues = "239081")
    public void listenerMessage(Email email) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username, false));


        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
        msg.setSubject(email.getSubject());
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(email.getBody(), "text/plain; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);

        Transport.send(msg);
    }
}



