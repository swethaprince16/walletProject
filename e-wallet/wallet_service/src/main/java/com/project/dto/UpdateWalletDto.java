package com.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWalletDto
{
    private Long userId;
    private Long dailyLimit;
    private Integer dailyTransactionLimit;
}
