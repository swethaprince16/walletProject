package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BadRequestException;
import com.project.DataNotFoundException;
import com.project.WalletConstants;
import com.project.dto.*;
import com.project.enums.PaymentStatus;
import com.project.enums.ServiceType;
import com.project.model.Wallet;
import com.project.repository.WalletRepository;
import com.project.templates.MailTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

@Service
@Slf4j
public class WalletOperationService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void createWalletForNewUser(UserWalletCreationRequest userWalletCreationRequest) throws JsonProcessingException {
        long userId = userWalletCreationRequest.getUserId();
        Wallet newWallet = new Wallet(userId);
        log.info(String.format("new wallet details for userId %d are wallet %s", userId, newWallet.toString()));

        Wallet createdWallet  = walletRepository.save(newWallet);
        log.info(String.format("new wallet created for userId %d are wallet %s", userId, newWallet.toString()));

        SendMailNotification sendMailNotification = SendMailNotification.builder()
                .receiverMailId("swethaprince16@gmail.com")
                .message(String.format(MailTemplates.getWalletCreationMailContent().getMailBody(), userWalletCreationRequest.getUserName()))
                .serviceType(ServiceType.WALLET_SERVICE)
                .subject(MailTemplates.getWalletCreationMailContent().getMailSubject())
                .build();
        Future<SendResult<String, String>> send = kafkaTemplate.send(WalletConstants.SEND_NOTIFICATION_TOPIC, createdWallet.getUserId().toString(), objectMapper.writeValueAsString(sendMailNotification));


    }

    @Transactional
    public void deleteWalletForUserID(Long userId) {
        walletRepository.deleteByUserId(userId);
        log.info(String.format("Wallet deleted for userID: %d",userId ));
    }

    public Double getBalanceForUserId(Long userId) {
       return  walletRepository.findBalanceByUserId(userId);
    }

    public Wallet updateWalletDetails(UpdateWalletDto updateWalletDto) {
        try {
            Wallet wallet = walletRepository.findById(updateWalletDto.getUserId()).orElseThrow(
                    () -> new DataNotFoundException(String.format("No data for walletID %d", updateWalletDto.getUserId()))
            );
            wallet.setDailyTransactionLimit(updateWalletDto.getDailyTransactionLimit());
            wallet.setDailyLimit(updateWalletDto.getDailyLimit());
            return walletRepository.save(wallet);
        } catch (RuntimeException e) {
            throw new BadRequestException("Updation failed due to exception" + e.getMessage());
        }
    }

    public void updateWalletBalance(InitiateTransactionDto receivedData) throws JsonProcessingException {
        Wallet senderWallet  = walletRepository.findByUserId(receivedData.getSenderId());
        senderWallet.setBalance(senderWallet.getBalance()-receivedData.getAmount());
        walletRepository.save(senderWallet);

        Wallet receiverWallet  = walletRepository.findByUserId(receivedData.getReceiverId());
        receiverWallet.setBalance(receiverWallet.getBalance()+receivedData.getAmount());
        walletRepository.save(receiverWallet);

        SuccessfulTransactionDto successfulTransactionDto = SuccessfulTransactionDto.builder()
                .transactionId(receivedData.getTransactionId())
                .status(PaymentStatus.SUCCESFUL)
                .build();
        Future<SendResult<String, String>> send = kafkaTemplate.send(WalletConstants.SUCCESSFUL_TRANSACTION_TOPIC, receivedData.getTransactionId(), objectMapper.writeValueAsString(successfulTransactionDto));

    }
}
