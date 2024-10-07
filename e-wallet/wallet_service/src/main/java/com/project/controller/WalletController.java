package com.project.controller;

import com.project.WalletService;
import com.project.dto.UpdateWalletDto;
import com.project.model.Wallet;
import com.project.service.WalletOperationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wallet")
@RestController
@Slf4j
public class WalletController {
    @Autowired
    private WalletOperationService walletOperationService;

    @GetMapping("checkBalance/{userId}")
    public ResponseEntity<?> getBalanceForUserId(@PathVariable Long userId){
        Double balance =  walletOperationService.getBalanceForUserId(userId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Wallet> updateUserDetails(@RequestBody UpdateWalletDto updateWalletDto){
        return new ResponseEntity<>(walletOperationService.updateWalletDetails(updateWalletDto), HttpStatus.OK);
    }

}
