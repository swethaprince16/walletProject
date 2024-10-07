package com.project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.WalletConstants;
import com.project.dto.SendMailNotification;
import com.project.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SendMailService sendMailService;

    @KafkaListener(topics = WalletConstants.SEND_NOTIFICATION_TOPIC, groupId = "send_notification")
    public void sendNotification(ConsumerRecord receivedMessage) throws JsonProcessingException {
        SendMailNotification d = objectMapper.readValue(receivedMessage.value().toString(), SendMailNotification.class);
        log.info(String.format("Received Notification from Service: %s with message: %s", d.getServiceType().name(), d.toString()));
        sendMailService.sendMail(d);
    }
}
