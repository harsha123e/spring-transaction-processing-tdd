package com.altimetrik.transactionprocessingtdd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.exception.FileProcessingException;
import com.altimetrik.transactionprocessingtdd.exception.InvalidFileNameException;
import com.altimetrik.transactionprocessingtdd.exception.UnsupportedFileFormatException;

public interface TransactionService {

	public void processAndSaveTransactions(MultipartFile file)
			throws IOException, InvalidFileNameException, UnsupportedFileFormatException, FileProcessingException;

}