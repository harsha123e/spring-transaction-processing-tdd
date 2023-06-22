package com.altimetrik.transactionprocessingtdd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface TransactionService {

	public void processAndSaveTransactions(MultipartFile file) throws IOException;

}