package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.FILE_IS_EMPTY;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.INVALID_FILE_NAME;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.service.TransactionProcessorFactory;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionProcessorFactory transactionProcessorFactory;

	@Autowired
	public TransactionServiceImpl(TransactionProcessorFactory transactionProcessorFactory) {
		this.transactionProcessorFactory = transactionProcessorFactory;
	}

	@Override
	public void processAndSaveTransactions(MultipartFile file) throws IOException {

		if (file.isEmpty()) {
			throw new IllegalArgumentException(FILE_IS_EMPTY);
		}

		String filename = file.getOriginalFilename();
		if (filename == null) {
			throw new IllegalArgumentException(INVALID_FILE_NAME);
		}

		String fileExtension = getFileExtension(filename);
		transactionProcessorFactory.getTransactionProcessor(fileExtension).processTransactions(file);

	}

	private String getFileExtension(String filename) {
		int extensionIndex = filename.lastIndexOf('.');
		return (extensionIndex >= 0 && extensionIndex < filename.length() - 1) ? filename.substring(extensionIndex + 1)
				: "";
	}
}
