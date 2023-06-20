package com.altimetrik.transactionprocessingtdd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;

class TransactionServiceTest {

	@Mock
	private CardTransactionRepository cardTransactionRepository;

	@Mock
	private WalletTransactionRepository walletTransactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@Captor
	private ArgumentCaptor<CardTransaction> cardTransactionCaptor;

	@Captor
	private ArgumentCaptor<WalletTransaction> walletTransactionCaptor;

	@Test
	public void testProcessAndSaveTransaction_WithCSVFile_StoreTransactionsInDatabase() throws Exception {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/transactions.csv";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);

		transactionService.processAndSaveTransactions(fileContent);

		// Verify that the cardTransactionRepository's save method is called for card
		// transactions
		verify(cardTransactionRepository, times(2)).save(cardTransactionCaptor.capture());

		// Verify that the walletTransactionRepository's save method is called for
		// wallet transactions
		verify(walletTransactionRepository).save(walletTransactionCaptor.capture());

		// Retrieve the captured card transactions and perform assertions
		List<CardTransaction> capturedCardTransactions = cardTransactionCaptor.getAllValues();
		assertEquals(2, capturedCardTransactions.size());

		// Assert card transaction details for each captured transaction
		CardTransaction cardTransaction1 = capturedCardTransactions.get(0);
		assertEquals("1234567894", cardTransaction1.getTransactionId());

		CardTransaction cardTransaction2 = capturedCardTransactions.get(1);
		assertEquals("1234567894", cardTransaction2.getTransactionId());
		// Assert card transaction details for cardTransaction2

		// Retrieve the captured wallet transaction and perform assertions
		WalletTransaction capturedWalletTransaction = walletTransactionCaptor.getValue();
		assertEquals("9876543212", capturedWalletTransaction.getTransactionId());
	}

}
