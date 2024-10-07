package com.project.service;

import com.project.dto.SendMailNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(SendMailNotification notification){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("swethapvds@gmail.com");
        simpleMailMessage.setSubject(notification.getSubject());
        simpleMailMessage.setTo(notification.getReceiverMailId());
        simpleMailMessage.setText(notification.getMessage());
        javaMailSender.send(simpleMailMessage);

        log.info(String.format("notification mail sent to %s", notification.getReceiverMailId()));

    }
}
