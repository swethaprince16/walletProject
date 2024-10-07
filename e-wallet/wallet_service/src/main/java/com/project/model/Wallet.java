package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet")
public class Wallet {

//    	- walletId
//	- userId
//	- balance
//	- dailtLimit
//	- dailyTransactionLimit
//	- createdDate
//	- updateDate

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    @Column(nullable = false)
    private Long userId;

    public Wallet(Long userId) {
        this.userId = userId;
        this.balance = 0.0;
        this.dailyLimit = 10000L;
        this.dailyTransactionLimit =10;
    }

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    private Long dailyLimit;

    @Column(nullable = false)
    private Integer dailyTransactionLimit;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @CreationTimestamp
    private LocalDateTime updatedTime;
}
