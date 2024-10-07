package com.project.dto;

import lombok.*;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiateTransactionDto {
//    private Long walletId;
    private Long senderId;
    private Long receiverId;
    private Double amount;
    private String transactionId;
}
