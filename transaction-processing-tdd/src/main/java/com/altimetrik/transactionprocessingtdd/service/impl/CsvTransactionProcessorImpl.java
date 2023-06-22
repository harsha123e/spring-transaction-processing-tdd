package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.CSV_DELIMITER;
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
@Qualifier("csvTransactionProcessor")
public class CsvTransactionProcessorImpl implements TransactionProcessor {

	private CardTransactionRepository cardTransactionRepository;
	private WalletTransactionRepository walletTransactionRepository;

	@Autowired
	public CsvTransactionProcessorImpl(CardTransactionRepository cardTransactionRepository,
			WalletTransactionRepository walletTransactionRepository) {
		this.cardTransactionRepository = cardTransactionRepository;
		this.walletTransactionRepository = walletTransactionRepository;
	}

	@Override
	public void processTransactions(MultipartFile file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			reader.lines().skip(1).forEach(this::saveTransactionFromCSV);

		}
	}

	private void saveTransactionFromCSV(String record) {

		String[] fields = record.split(CSV_DELIMITER);

		String transactionId = fields[0];
		String transactionType = fields[1];
		double amount = Double.parseDouble(fields[5]);
		String remarks = fields[6];

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transactionType)) {

			String cardNumber = fields[2];
			LocalDate expiryDate = LocalDate.parse(fields[3], DATE_FORMATTER);
			int cvv = Integer.parseInt(fields[4]);

			cardTransactionRepository
					.save(new CardTransaction(transactionId, cardNumber, expiryDate, cvv, amount, remarks));
		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transactionType)) {

			String walletType = fields[2];
			String upiId = fields[3];
			double balance = Double.parseDouble(fields[4]);

			walletTransactionRepository
					.save(new WalletTransaction(transactionId, walletType, upiId, balance, amount, remarks));
		}
	}
}
