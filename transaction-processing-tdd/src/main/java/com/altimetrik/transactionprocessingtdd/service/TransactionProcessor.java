package com.altimetrik.transactionprocessingtdd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface TransactionProcessor {
	void processTransactions(MultipartFile file) throws IOException;
}
