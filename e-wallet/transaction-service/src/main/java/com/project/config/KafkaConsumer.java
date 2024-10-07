package com.project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.WalletConstants;
import com.project.dto.InitiateTransactionDto;
import com.project.dto.SuccessfulTransactionDto;
import com.project.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Slf4j


public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = WalletConstants.SUCCESSFUL_TRANSACTION_TOPIC, groupId = "successful-transaction")
    public void pollSuccessfulOrFailureTransaction(ConsumerRecord receivedMessage) throws JsonProcessingException {
        SuccessfulTransactionDto receivedData = objectMapper.readValue(receivedMessage.value().toString(), SuccessfulTransactionDto.class);
        log.info("ReceivedData:{}", receivedData.toString());
        transactionService.updateTransactionStatus(receivedData);
        transactionService.sendUserNotification(receivedData);
    }

}
