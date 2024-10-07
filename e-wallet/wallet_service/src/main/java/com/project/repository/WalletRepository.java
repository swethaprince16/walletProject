package com.project.repository;

import com.project.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    void deleteByUserId(Long userId);

    @Query("SELECT w.balance FROM Wallet w WHERE w.userId = :userId")
    Double findBalanceByUserId(Long userId);
    Wallet findByUserId(Long userId);
}
