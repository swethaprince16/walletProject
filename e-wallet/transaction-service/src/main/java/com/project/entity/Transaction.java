package com.project.entity;

import com.project.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wallet_transaction")
public class Transaction {
//    	- transactionId
//	- wallterId
//	- senderId
//	- receiverId
//	- amount
//	- paymentStaus
//	- remark - SUCCES, PENDING, FAILED
//	- paymentDate

    @Id
    @UuidGenerator
    private String transactionId;
    @Column(nullable = false)
    private Long walletId;
    @Column(nullable = false)
    private Long senderId;
    @Column(nullable = false)
    private Long receiverId;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String remark;
    @CreationTimestamp
    private LocalDateTime paymentInitiationDate;
    @CreationTimestamp
    private LocalDateTime paymentSuccessfulDate;

//    public Transaction(Long walletId, Long senderId, Long receiverId, Double amount) {
//        this.walletId = walletId;
//        this.senderId = senderId;
//        this.receiverId = receiverId;
//        this.amount = amount;
//    }

    public Transaction(Long walletId, Long senderId, Long receiverId, Double amount) {
        this.walletId = walletId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }
}
