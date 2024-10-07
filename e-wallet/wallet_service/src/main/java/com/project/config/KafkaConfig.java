package com.project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.WalletConstants;
import com.project.dto.InitiateTransactionDto;
import com.project.dto.UserWalletCreationRequest;
import com.project.service.WalletOperationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

@Configuration
@Slf4j
public class KafkaConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletOperationService walletOperationService;


    @KafkaListener(topics = WalletConstants.USER_CREATION_TOPIC, groupId = "user-service.walletCreation")
    public void getMessageForUserCreation(ConsumerRecord receivedMessage) throws JsonProcessingException {
        UserWalletCreationRequest receivedData = objectMapper.readValue(receivedMessage.value().toString(), UserWalletCreationRequest.class);
        log.info("ReceivedData:{}", receivedData.toString());
        walletOperationService.createWalletForNewUser(receivedData);


    }

    @KafkaListener(topics = WalletConstants.USER_DELETION_TOPIC,  groupId = "user-service.walletDeletion")
    public void getMessageForUserWalletDeletion(ConsumerRecord receivedMessage) throws JsonProcessingException {
//        Map<String,Long> receivedData = objectMapper.readValue(receivedMessage.value().toString(), Map.class);
        Long receivedData = objectMapper.readValue(receivedMessage.value().toString(), Long.class);

        log.info("ReceivedData:{}", receivedData.toString());

        walletOperationService.deleteWalletForUserID(receivedData);
    }
    @KafkaListener(topics = WalletConstants.INITIATE_TRANSACTION_TOPIC, groupId = "INITIATE_TRANSACTION_TOPIC")
    public void pollMessageForBalanceUpdate(ConsumerRecord receivedMessage) throws JsonProcessingException {
        InitiateTransactionDto receivedData = objectMapper.readValue(receivedMessage.value().toString(), InitiateTransactionDto.class);
        log.info("ReceivedData:{}", receivedData.toString());
        walletOperationService.updateWalletBalance(receivedData);
    }
}
