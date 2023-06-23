package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;
import com.altimetrik.transactionprocessingtdd.service.TransactionProcessor;

@Service
@Qualifier("csvTransactionProcessor")
public class CsvTransactionProcessorImpl implements TransactionProcessor {

	private CardTransactionRepository cardTransactionRepository;
	private WalletTransactionRepository walletTransactionRepository;
	private ModelMapper mapper;

	@Autowired
	public CsvTransactionProcessorImpl(CardTransactionRepository cardTransactionRepository,
			WalletTransactionRepository walletTransactionRepository, ModelMapper mapper) {
		super();
		this.cardTransactionRepository = cardTransactionRepository;
		this.walletTransactionRepository = walletTransactionRepository;
		this.mapper = mapper;
	}

	@Override
	public void processTransactions(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException(FILE_IS_EMPTY);
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			reader.lines().skip(1).forEach(this::saveTransactionFromCSV);
		}
	}

	private void saveTransactionFromCSV(String record) {
		String[] fields = record.split(CSV_DELIMITER);

		if (isValidFields(fields)) {
			String transactionType = fields[1];

			if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transactionType)) {
				saveCardTransaction(fields);
			} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transactionType)) {
				saveWalletTransaction(fields);
			}
		}
	}

	private boolean isValidFields(String[] fields) {
		if (fields.length != 7) {
			return false;
		}
		for (String field : fields) {
			if (field.isBlank() || field.isEmpty() || field == null) {
				return false;
			}
		}
		String transactionType = fields[1];
		if (!isValidTransactionType(transactionType)) {
			return false;
		} else {
			if (transactionType.equalsIgnoreCase(TRANSACTIONTYPE_CARD)) {
				return isValidCardTransactionFields(fields);
			} else if (transactionType.equalsIgnoreCase(TRANSACTIONTYPE_WALLET)) {
				return isValidWalletTransactionFields(fields);
			}
		}
		return true;
	}

	private boolean isValidTransactionType(String string) {
		return TRANSACTIONTYPE_CARD.equalsIgnoreCase(string) || TRANSACTIONTYPE_WALLET.equalsIgnoreCase(string);
	}

	private boolean isValidCardTransactionFields(String[] fields) {
		try {
			LocalDate.parse(fields[3], DATE_FORMATTER);
			Integer.parseInt(fields[4]);
			Double.parseDouble(fields[5]);
		} catch (NumberFormatException | DateTimeParseException e) {
			return false;
		}
		return true;
	}

	private boolean isValidWalletTransactionFields(String[] fields) {
		try {
			Double.parseDouble(fields[4]);
			Double.parseDouble(fields[5]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private void saveWalletTransaction(String[] fields) {
		WalletTransaction walletTransaction = mapper.map(fields, WalletTransaction.class);
		walletTransactionRepository.save(walletTransaction);
	}

	private void saveCardTransaction(String[] fields) {
		CardTransaction cardTransaction = mapper.map(fields, CardTransaction.class);
		cardTransactionRepository.save(cardTransaction);
	}
}
