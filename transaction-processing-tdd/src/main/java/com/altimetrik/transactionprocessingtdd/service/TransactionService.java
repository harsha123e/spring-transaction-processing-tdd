package com.altimetrik.transactionprocessingtdd.service;

public interface TransactionService {

	boolean processAndSaveTransactions(byte[] fileContent);

}