package com.altimetrik.transactionprocessingtdd.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.altimetrik.transactionprocessingtdd.service.TransactionProcessor;
import com.altimetrik.transactionprocessingtdd.service.TransactionProcessorFactory;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

@Component
public class DefaultTransactionProcessorFactory implements TransactionProcessorFactory {

	private final TransactionProcessor csvTransactionProcessor;
	private final TransactionProcessor excelTransactionProcessor;
	private final TransactionProcessor fixedFileLengthTransactionProcessor;

	public DefaultTransactionProcessorFactory(
			@Qualifier("csvTransactionProcessor") TransactionProcessor csvTransactionProcessor,
			@Qualifier("excelTransactionProcessor") TransactionProcessor excelTransactionProcessor,
			@Qualifier("fixedFileLengthTransactionProcessor") TransactionProcessor fixedFileLengthTransactionProcessor) {
		this.csvTransactionProcessor = csvTransactionProcessor;
		this.excelTransactionProcessor = excelTransactionProcessor;
		this.fixedFileLengthTransactionProcessor = fixedFileLengthTransactionProcessor;
	}

	@Override
	public TransactionProcessor getTransactionProcessor(String fileExtension) {
		switch (fileExtension) {
		case CSV_EXTENSION:
			return csvTransactionProcessor;
		case EXCEL_EXTENSION:
			return excelTransactionProcessor;
		case FIXEDLENGTHFILEFORMAT_EXTENSION:
			return fixedFileLengthTransactionProcessor;
		default:
			throw new IllegalArgumentException("Unsupported file extension: " + fileExtension);
		}
	}
}
