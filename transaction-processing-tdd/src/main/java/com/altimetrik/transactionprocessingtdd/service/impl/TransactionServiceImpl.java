package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.service.TransactionProcessor;
import com.altimetrik.transactionprocessingtdd.service.TransactionProcessorFactory;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;
import com.altimetrik.transactionprocessingtdd.utils.FileUtil;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionProcessorFactory transactionProcessorFactory;

	@Autowired
	public TransactionServiceImpl(TransactionProcessorFactory transactionProcessorFactory) {
		this.transactionProcessorFactory = transactionProcessorFactory;
	}

	@Override
	public void processAndSaveTransactions(MultipartFile file, String fileExtension) throws IOException {

		if (file == null) {
			throw new IllegalArgumentException(FILE_IS_EMPTY);
		}
		if (fileExtension == null || fileExtension.isEmpty() || fileExtension.isBlank()
				|| !FileUtil.isValidExtension(fileExtension)) {
			throw new IllegalArgumentException(INVALID_FILE_EXTENSION);
		}
		TransactionProcessor transactionProcessor = transactionProcessorFactory.getTransactionProcessor(fileExtension);
		if (transactionProcessor == null) {
			throw new IllegalArgumentException(NO_SUCH_TRANSACTION_PROCESSOR + fileExtension);
		}
		transactionProcessor.processTransactions(file);

	}

}
