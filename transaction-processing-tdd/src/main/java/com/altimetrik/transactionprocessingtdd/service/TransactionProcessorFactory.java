package com.altimetrik.transactionprocessingtdd.service;

public interface TransactionProcessorFactory {
	TransactionProcessor getTransactionProcessor(String fileExtension);
}