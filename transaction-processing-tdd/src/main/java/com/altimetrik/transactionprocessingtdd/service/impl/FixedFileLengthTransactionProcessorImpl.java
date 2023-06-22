package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.DATE_FORMATTER;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.TRANSACTIONTYPE_CARD;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.TRANSACTIONTYPE_WALLET;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

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
@Qualifier("fixedFileLengthTransactionProcessor")
public class FixedFileLengthTransactionProcessorImpl implements TransactionProcessor {

	private CardTransactionRepository cardTransactionRepository;
	private WalletTransactionRepository walletTransactionRepository;

	@Autowired
	public FixedFileLengthTransactionProcessorImpl(CardTransactionRepository cardTransactionRepository,
			WalletTransactionRepository walletTransactionRepository) {
		this.cardTransactionRepository = cardTransactionRepository;
		this.walletTransactionRepository = walletTransactionRepository;
	}

	@Override
	public void processTransactions(MultipartFile file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			reader.lines().skip(1).forEach(this::saveTransactionFromFixedLengthFile);

		}
	}

	private void saveTransactionFromFixedLengthFile(String record) {
		String transactionId = record.substring(0, 10).trim();
		String transactionType = record.substring(11, 26).trim();
		double amount = Double.parseDouble(record.substring(55, 67).trim());
		String remarks = record.substring(67).trim();

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transactionType)) {
			String cardNumber = record.substring(26, 39).trim();
			int cvv = Integer.parseInt(record.substring(40, 43).trim());
			LocalDate expiryDate = LocalDate.parse(record.substring(44, 54).trim(), DATE_FORMATTER);

			cardTransactionRepository
					.save(new CardTransaction(transactionId, cardNumber, expiryDate, cvv, amount, remarks));
		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transactionType)) {
			String walletType = record.substring(26, 39).trim();
			String upiId = record.substring(40, 43).trim();
			double balance = Double.parseDouble(record.substring(44, 54).trim());

			walletTransactionRepository
					.save(new WalletTransaction(transactionId, walletType, upiId, balance, amount, remarks));
		}
	}
}
