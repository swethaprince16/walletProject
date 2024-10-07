package com.project.dto;

import com.project.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessfulTransactionDto {
    private String transactionId;
    private PaymentStatus status;
}
