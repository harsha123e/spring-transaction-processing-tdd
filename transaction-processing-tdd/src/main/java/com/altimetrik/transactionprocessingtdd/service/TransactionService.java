package com.altimetrik.transactionprocessingtdd.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.exception.EmptyFileException;
import com.altimetrik.transactionprocessingtdd.exception.UnsupportedFileFormatException;

public interface TransactionService {

	public void processAndSaveTransactions(MultipartFile file)
			throws IOException, EmptyFileException, UnsupportedFileFormatException;

}