package com.project.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.web.bind.annotation.DeleteMapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTransactionRequestDto {

    @Min(1)
    private Long walletId;
    @Min(1)
    private Long senderId;
    @Min(1)
    private Long receiverId;
    @DecimalMin("1.0")
    private Double amount;
}
