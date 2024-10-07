package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DataNotFoundException;
import com.project.WalletConstants;
import com.project.dto.*;
import com.project.enums.PaymentStatus;
import com.project.entity.Transaction;
import com.project.enums.ServiceType;
import com.project.repository.TransactionRepository;
import com.project.templates.MailTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Slf4j
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public UserTransactionResponseDto sendUserTransactionRequest(UserTransactionRequestDto request) throws JsonProcessingException {
        Boolean senderExistCheck = restTemplate.getForObject("http://localhost:8082/wallet-users/userExistence/"+request.getSenderId(), Boolean.class);
        if(Boolean.FALSE.equals(senderExistCheck)){
            log.info(String.format("Sender with id %d does not exist",request.getSenderId()));
            throw new DataNotFoundException(String.format("Sender with id %d does not exist",request.getSenderId()));
        }

        Boolean receiverExistCheck = restTemplate.getForObject("http://localhost:8082/wallet-users/userExistence/"+request.getReceiverId(), Boolean.class);
        if(Boolean.FALSE.equals(receiverExistCheck)){
            log.info(String.format("Receiver with id %d does not exist",request.getReceiverId()));
            throw new DataNotFoundException(String.format("Receiver with id %d does not exist",request.getReceiverId()));
        }

        Double userBalance = restTemplate.getForObject("http://localhost:8082/wallet/checkBalance/"+request.getSenderId(), Double.class);
        if(request.getAmount()> userBalance){
            log.info(String.format("Insufficient balance. Available balance is %f",userBalance));
            throw new DataNotFoundException(String.format("Insufficient balance. Available balalance is %f",userBalance));
        }

        Transaction currentTransaction  = new Transaction(request.getWalletId(), request.getSenderId(), request.getReceiverId(), request.getAmount());
        Transaction savedTransaction = transactionRepository.save(currentTransaction);

        InitiateTransactionDto initiateTransactionDto = InitiateTransactionDto.builder()
                .transactionId(savedTransaction.getTransactionId())
                .senderId(savedTransaction.getSenderId())
                .amount(savedTransaction.getAmount())
//                .walletId(savedTransaction.getWalletId())
                .receiverId(savedTransaction.getReceiverId())
                .build();
        Future<SendResult<String, String>> send = kafkaTemplate.send(WalletConstants.INITIATE_TRANSACTION_TOPIC, savedTransaction.getTransactionId(), objectMapper.writeValueAsString(initiateTransactionDto));


        return UserTransactionResponseDto.builder()
                .transactionId(savedTransaction.getTransactionId())
                .message("Transaction in progess...")
                .status(PaymentStatus.PENDING)
                .paymentInitiationTime(savedTransaction.getPaymentInitiationDate())
                .build();
    }

    public void updateTransactionStatus(SuccessfulTransactionDto receivedData) {
        Transaction transaction = transactionRepository.findById(receivedData.getTransactionId()).orElseThrow(
                ()->new DataNotFoundException(String.format("No transaction found for Transactio ID : %d", receivedData.getTransactionId()))
        );
        transaction.setStatus(
                receivedData.getStatus().equals(PaymentStatus.SUCCESFUL)?PaymentStatus.SUCCESFUL:PaymentStatus.REJECTED
        );
        transactionRepository.save(transaction);
    }

    public void sendUserNotification(SuccessfulTransactionDto receivedData) throws JsonProcessingException {
        Transaction transaction  = transactionRepository.findById(receivedData.getTransactionId()).get();
        UserDetailsDto senderDetails= restTemplate.getForObject("http://localhost:8082/wallet-users/getUserDetails/"+transaction.getSenderId(), UserDetailsDto.class);
        UserDetailsDto receiverDetails= restTemplate.getForObject("http://localhost:8082/wallet-users/getUserDetails/"+transaction.getReceiverId(), UserDetailsDto.class);

        SendMailNotification sendMailNotification = SendMailNotification.builder()
                .receiverMailId("swethaprince16@gmail.com")
                .message(String.format(MailTemplates.getTransactionSuccessMessage().getMailBody(), receiverDetails.getUserName(), transaction.getTransactionId()))
                .serviceType(ServiceType.TRANSACTION_SERVICE)
                .subject(MailTemplates.getTransactionSuccessMessage().getMailSubject())
                .build();

        Future<SendResult<String, String>> send = kafkaTemplate.send(WalletConstants.SEND_NOTIFICATION_TOPIC, transaction.getTransactionId(), objectMapper.writeValueAsString(sendMailNotification));


    }
}
