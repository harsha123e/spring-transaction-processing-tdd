package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_CSV;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_EXCEL;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_TXT;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_RESOURCE;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.DATE_FORMATTER;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_FILE_RESOURCE;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_PARAM;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.TEXT_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.TEXT_FILE_RESOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest2 {

	@Mock
	private CardTransactionRepository cardTransactionRepository;

	@Mock
	private WalletTransactionRepository walletTransactionRepository;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Captor
	private ArgumentCaptor<CardTransaction> cardTransactionCaptor;

	@Captor
	private ArgumentCaptor<WalletTransaction> walletTransactionCaptor;

	@Test
	public void testProcessAndSaveTransaction_WithCSVFile_StoreTransactionsInDatabase() throws Exception {

		byte[] fileContent = Files.readAllBytes(Paths.get(CSV_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		transactionService.processAndSaveTransactions(file);

		verify(cardTransactionRepository, times(2)).save(cardTransactionCaptor.capture());
		verify(walletTransactionRepository, times(2)).save(walletTransactionCaptor.capture());

		assertEquals(2, cardTransactionCaptor.getAllValues().size());
		assertEquals(2, walletTransactionCaptor.getAllValues().size());

		assertCardTransaction(cardTransactionCaptor.getAllValues().get(0), "123424313", "876998799879",
				LocalDate.parse("23-01-2020", DATE_FORMATTER), 123, 123.412, "card processing");
		assertCardTransaction(cardTransactionCaptor.getAllValues().get(1), "12346234213", "876998799879",
				LocalDate.parse("23-01-2020", DATE_FORMATTER), 123, 123.412, "card processing");

		WalletTransaction capturedWalletTransaction1 = walletTransactionCaptor.getAllValues().get(0);
		assertEquals("1234653213", capturedWalletTransaction1.getTransactionId());

		WalletTransaction capturedWalletTransaction2 = walletTransactionCaptor.getAllValues().get(1);
		assertEquals("1265234213", capturedWalletTransaction2.getTransactionId());
	}

	@Test
	public void testProcessAndSaveTransaction_WithExcelFile_StoreTransactionsInDatabase() throws Exception {

		byte[] fileContent = Files.readAllBytes(Paths.get(EXCEL_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, EXCEL_FILE_NAME, CONTENT_TYPE_EXCEL, fileContent);

		transactionService.processAndSaveTransactions(file);

		verify(cardTransactionRepository, times(2)).save(cardTransactionCaptor.capture());

		verify(walletTransactionRepository, times(2)).save(walletTransactionCaptor.capture());

		assertEquals(2, cardTransactionCaptor.getAllValues().size());
		assertEquals(2, walletTransactionCaptor.getAllValues().size());

		assertCardTransaction(cardTransactionCaptor.getAllValues().get(0), "123424313", "876998799879",
				LocalDate.parse("23-01-2020", DATE_FORMATTER), 123, 123.412, "card processing");
		assertCardTransaction(cardTransactionCaptor.getAllValues().get(1), "12346234213", "876998799879",
				LocalDate.parse("24-01-2020", DATE_FORMATTER), 124, 124.412, "card processing");

		WalletTransaction capturedWalletTransaction1 = walletTransactionCaptor.getAllValues().get(0);
		assertEquals("1234653213", capturedWalletTransaction1.getTransactionId());

		WalletTransaction capturedWalletTransaction2 = walletTransactionCaptor.getAllValues().get(1);
		assertEquals("1265234213", capturedWalletTransaction2.getTransactionId());
	}

	@Test
	public void testProcessAndSaveTransaction_WithFixedFormatLengthFile_StoreTransactionsInDatabase() throws Exception {

		byte[] fileContent = Files.readAllBytes(Paths.get(TEXT_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, TEXT_FILE_NAME, CONTENT_TYPE_TXT, fileContent);

		transactionService.processAndSaveTransactions(file);

		verify(cardTransactionRepository, times(2)).save(cardTransactionCaptor.capture());

		verify(walletTransactionRepository).save(walletTransactionCaptor.capture());

		List<CardTransaction> capturedCardTransactions = cardTransactionCaptor.getAllValues();
		assertEquals(2, capturedCardTransactions.size());

		CardTransaction cardTransaction1 = capturedCardTransactions.get(0);
		assertEquals("1234567894", cardTransaction1.getTransactionId());

		CardTransaction cardTransaction2 = capturedCardTransactions.get(1);
		assertEquals("4567890125", cardTransaction2.getTransactionId());

		WalletTransaction capturedWalletTransaction = walletTransactionCaptor.getValue();
		assertEquals("9876543212", capturedWalletTransaction.getTransactionId());
	}

	private void assertCardTransaction(CardTransaction cardTransaction, String transactionId, String cardNumber,
			LocalDate expiryDate, int cvv, double amount, String remarks) {
		assertEquals(transactionId, cardTransaction.getTransactionId());
		assertEquals(cardNumber, cardTransaction.getCardNumber());
		assertEquals(expiryDate, cardTransaction.getExpiryDate());
		assertEquals(cvv, cardTransaction.getCvv());
		assertEquals(amount, cardTransaction.getAmount());
		assertEquals(remarks, cardTransaction.getRemarks());
	}

}
