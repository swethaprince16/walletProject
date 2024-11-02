package com.project.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.dto.UpdateUserDto;
import com.project.dto.UserDetailsDto;
import com.project.model.User;
import com.project.dto.UserResponseDto;
import com.project.service.UserOperationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/wallet-users")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserOperationService userOperationService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUserAccount(@RequestBody @Valid User user) {
        try {
            return new ResponseEntity<>(userOperationService.createUserAccount(user), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error(String.format("runtime exception occured for current userId: %d Exception is : %s", user.getUserId(), e.getMessage()));
        } catch (JsonProcessingException e) {
            log.error(String.format("JsonProcessingException occured for current userId: %d Exception is : %s", user.getUserId(), e.getMessage()));
        }
        return null;
    }

    @GetMapping("/getUserDetails/{userId}")
    public ResponseEntity<UserDetailsDto> getUserDetailsById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userOperationService.getUserDetailsById(userId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUserDetails(@RequestBody UpdateUserDto updateUserDto){
        return new ResponseEntity<>(userOperationService.updateUserDetails(updateUserDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId){
        userOperationService.deleteUserById(userId);
        return new ResponseEntity<>(String.format("User id %d successfully deleted", userId), HttpStatus.OK);
    }

    @GetMapping("/userExistence/{id}")
    public ResponseEntity<Boolean> checkfUserExistOrNot(@PathVariable("id") Long userId){
        return new ResponseEntity<>(userOperationService.checkfUserExistOrNot(userId), HttpStatus.OK);
    }

    @GetMapping("/getMailId/{id}")
    public ResponseEntity<String> getMailId(@PathVariable("id") Long userId){
        return new ResponseEntity<>(userOperationService.getMailId(userId), HttpStatus.OK);
    }
}
