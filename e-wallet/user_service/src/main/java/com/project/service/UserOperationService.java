package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BadRequestException;
import com.project.DataNotFoundException;
import com.project.WalletConstants;
import com.project.dto.UpdateUserDto;
import com.project.dto.UserDetailsDto;
import com.project.dto.UserWalletCreationRequest;
import com.project.model.User;
import com.project.dto.UserResponseDto;
import com.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
@Slf4j
public class UserOperationService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    public UserResponseDto createUserAccount(User user) throws JsonProcessingException {

        userRepository.save(user);
//        Map<String, Long > walletCreationRequest = Map.of("userId", user.getUserId());
        log.info(String.format("User account is created with id: %d and username %s", user.getUserId(), user.getUserName()));
        UserWalletCreationRequest userWalletCreationRequest = UserWalletCreationRequest.builder()
                .userName(user.getUserName())
                .userId(user.getUserId())
                .userEmailId(user.getEmailId())
                .build();

        Future<SendResult<String, String>> send = kafkaTemplate.send(WalletConstants.USER_CREATION_TOPIC, user.getUserId().toString(), objectMapper.writeValueAsString(userWalletCreationRequest));
        return UserResponseDto.builder()
                .message("user account is created. Wallet creation is in progress. Will be notified via mail")
                .user(user)
                .build();
    }

    public UserDetailsDto getUserDetailsById(Long userId) {

        User user =  userRepository.findById(userId).orElseThrow(
                ()->new DataNotFoundException(String.format("No data for userID %d", userId))
        );
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .userName(user.getUserName())
                .address(user.getAddress())
                .mobileNumber(user.getMobileNumber())
                .emailId(user.getEmailId())
                .build();
        return userDetailsDto;
    }

    public User updateUserDetails(UpdateUserDto updateUserDto) {
        try {
            User user = userRepository.findById(updateUserDto.getUserId()).orElseThrow(
                    () -> new DataNotFoundException(String.format("No data for userID %d", updateUserDto.getUserId()))
            );
            user.setMobileNumber(updateUserDto.getMobileNumber());
            user.setAddress(updateUserDto.getAddress());
            user.setEmailId(updateUserDto.getEmailId());
            return userRepository.save(user);
        } catch (RuntimeException e) {
            throw new BadRequestException("Updation failed due to exception" + e.getMessage());
        }
    }

    public void deleteUserById (Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new DataNotFoundException(String.format("No data for userID %d", userId))
            );
            userRepository.delete(user);
            log.info(String.format("User details deleted for userID: %d.",userId ));

//            Map<String, Long> deletionData = Map.of("userId", userId);
            Future<SendResult<String, String>> send = kafkaTemplate.send(WalletConstants.USER_DELETION_TOPIC, userId.toString(), objectMapper.writeValueAsString(userId));

        } catch (RuntimeException e) {
            throw new BadRequestException("Deletion failed due to exception: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkfUserExistOrNot(Long userId) {
        return userRepository.existsById(userId);
    }

    public String getMailId(Long userId) {
        return userRepository.findEmailIdByUserId(userId);
    }
}
