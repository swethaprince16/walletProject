package com.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.dto.UserTransactionRequestDto;
import com.project.dto.UserTransactionResponseDto;
import com.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/transaction")
@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping
    public UserTransactionResponseDto sendUserTransactionRequest(@RequestBody UserTransactionRequestDto request) throws JsonProcessingException {
        return transactionService.sendUserTransactionRequest(request);
    }

}
