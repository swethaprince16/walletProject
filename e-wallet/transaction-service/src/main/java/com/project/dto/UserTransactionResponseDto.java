package com.project.dto;

import com.project.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTransactionResponseDto {
    private String message;
    private String transactionId;
    private PaymentStatus status;
    private LocalDateTime paymentInitiationTime;
}
