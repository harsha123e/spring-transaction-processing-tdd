package com.altimetrik.transactionprocessingtdd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.altimetrik.transactionprocessingtdd.service.impl.TransactionServiceImpl;
import com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

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

		String filePath = TestTransactionProcessingConstants.CSV_FILE_RESOURCE;

		String controllerParam = "file";
		String originalFileName = "transactions.csv";
		String contentType = "text/csv";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		transactionService.processAndSaveTransactions(file);

		verify(cardTransactionRepository, times(2)).save(cardTransactionCaptor.capture());

		verify(walletTransactionRepository, times(2)).save(walletTransactionCaptor.capture());

		List<CardTransaction> capturedCardTransactions = cardTransactionCaptor.getAllValues();
		assertEquals(2, capturedCardTransactions.size());

		CardTransaction cardTransaction1 = capturedCardTransactions.get(0);
		assertEquals("123424313", cardTransaction1.getTransactionId());
		assertEquals("876998799879", cardTransaction1.getCardNumber());
		assertEquals(LocalDate.parse("23-01-2020", formatter), cardTransaction1.getExpiryDate());
		assertEquals(123, cardTransaction1.getCvv());
		assertEquals(123.412, cardTransaction1.getAmount());
		assertEquals("card processing", cardTransaction1.getRemarks());

		CardTransaction cardTransaction2 = capturedCardTransactions.get(1);
		assertEquals("12346234213", cardTransaction2.getTransactionId());

		List<WalletTransaction> capturedWalletTransactions = walletTransactionCaptor.getAllValues();
		assertEquals(2, capturedCardTransactions.size());

		WalletTransaction capturedWalletTransaction1 = capturedWalletTransactions.get(0);
		assertEquals("1234653213", capturedWalletTransaction1.getTransactionId());

		WalletTransaction capturedWalletTransaction2 = capturedWalletTransactions.get(1);
		assertEquals("1265234213", capturedWalletTransaction2.getTransactionId());
	}

	@Test
	public void testProcessAndSaveTransaction_WithExcelFile_StoreTransactionsInDatabase() throws Exception {

		String filePath = TestTransactionProcessingConstants.EXCEL_FILE_RESOURCE;

		String controllerParam = "file";
		String originalFileName = "transactions.xlsx";
		String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		transactionService.processAndSaveTransactions(file);

		verify(cardTransactionRepository, times(2)).save(cardTransactionCaptor.capture());

		verify(walletTransactionRepository, times(2)).save(walletTransactionCaptor.capture());

		List<CardTransaction> capturedCardTransactions = cardTransactionCaptor.getAllValues();
		assertEquals(2, capturedCardTransactions.size());

		CardTransaction cardTransaction1 = capturedCardTransactions.get(0);
		assertEquals("123424313", cardTransaction1.getTransactionId());
		assertEquals("876998799879", cardTransaction1.getCardNumber());
		assertEquals(LocalDate.parse("23-01-2020", formatter), cardTransaction1.getExpiryDate());
		assertEquals(123, cardTransaction1.getCvv());
		assertEquals(123.412, cardTransaction1.getAmount());
		assertEquals("card processing", cardTransaction1.getRemarks());

		CardTransaction cardTransaction2 = capturedCardTransactions.get(1);
		assertEquals("12346234213", cardTransaction2.getTransactionId());

		List<WalletTransaction> capturedWalletTransactions = walletTransactionCaptor.getAllValues();
		assertEquals(2, capturedCardTransactions.size());

		WalletTransaction capturedWalletTransaction1 = capturedWalletTransactions.get(0);
		assertEquals("1234653213", capturedWalletTransaction1.getTransactionId());

		WalletTransaction capturedWalletTransaction2 = capturedWalletTransactions.get(1);
		assertEquals("1265234213", capturedWalletTransaction2.getTransactionId());
	}

	@Test
	public void testProcessAndSaveTransaction_WithFixedFormatLengthFile_StoreTransactionsInDatabase() throws Exception {

		String filePath = TestTransactionProcessingConstants.TEXT_FILE_RESOURCE;

		String controllerParam = "file";
		String originalFileName = "transactions.txt";
		String contentType = "text/plain;charset=UTF-8";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

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

}
