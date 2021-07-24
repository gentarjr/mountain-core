package com.mountain.repo;

import com.mountain.entity.detail.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Transaction findById(String transactionId);
}
