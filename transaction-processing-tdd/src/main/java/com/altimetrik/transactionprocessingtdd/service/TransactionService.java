package com.altimetrik.transactionprocessingtdd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface TransactionService {

	void processAndSaveTransactions(MultipartFile file, String fileExtension) throws IOException;
}