package com.altimetrik.transactionprocessingtdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, String> {

}
