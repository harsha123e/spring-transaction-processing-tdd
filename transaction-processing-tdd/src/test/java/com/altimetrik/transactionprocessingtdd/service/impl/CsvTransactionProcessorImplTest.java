package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_CSV;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_PARAM;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;

@ExtendWith(MockitoExtension.class)
class CsvTransactionProcessorImplTest {

	@Mock
	private CardTransactionRepository cardTransactionRepository;

	@Mock
	private WalletTransactionRepository walletTransactionRepository;

	@InjectMocks
	private CsvTransactionProcessorImpl csvTransactionProcessor;

	@Test
	@DisplayName("If file is null - throw exception")
	void shouldThrowIllegalArgumentExceptionForInvalidFile() {
		assertThrows(IllegalArgumentException.class, () -> csvTransactionProcessor.processTransactions(null));
		verify(cardTransactionRepository, never()).save(any(CardTransaction.class));
		verify(walletTransactionRepository, never()).save(any(WalletTransaction.class));
	}

	@Test
	@DisplayName("If file only has header - don't save data")
	void shouldSkipFileIfOnlyHeaderIsPresent() throws IOException {
		byte[] fileContent = "header,header,header\n".getBytes();
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		csvTransactionProcessor.processTransactions(file);

		verify(cardTransactionRepository, never()).save(any(CardTransaction.class));
		verify(walletTransactionRepository, never()).save(any(WalletTransaction.class));
	}

	@Test
	@DisplayName("If file has valid transactions - save transactions")
	void shouldSaveAllTransactionInFile() throws IOException {
		String csvData = "transaction id,type,card number/wallettype,expiry date/upi id,cvv/balance,amount,remarks\n"
				+ "1,card,1234567890123456,12-12-2023,123,100.0,Card Transaction\n"
				+ "2,wallet,digi-wallet,paytm@user,456.00,200.0,Wallet Transaction\n";

		byte[] fileContent = csvData.getBytes();
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		csvTransactionProcessor.processTransactions(file);

		verify(cardTransactionRepository, times(1)).save(any(CardTransaction.class));
		verify(walletTransactionRepository, times(1)).save(any(WalletTransaction.class));
	}

	@Test
	@DisplayName("If file has invalid transactions - skip transactions")
	void shouldSkipInvalidTransactionInFile() throws IOException {
		String csvData = "transaction id,type,card number/wallettype,expiry date/upi id,cvv/balance,amount,remarks\n"
				+ "1,card,12-12-2023,123,100.0,Card Transaction\n"
				+ "2,wallet,digi-wallet,paytm@user,456.00,Wallet Transaction\n";

		byte[] fileContent = csvData.getBytes();
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		csvTransactionProcessor.processTransactions(file);

		verify(cardTransactionRepository, never()).save(any(CardTransaction.class));
		verify(walletTransactionRepository, never()).save(any(WalletTransaction.class));
	}

	@Test
	@DisplayName("If file has invalid numeric transactions - skip transactions")
	void shouldSkipInValidTransactionWithIncorrectNumericValuesInFile() throws IOException {
		String csvData = "transaction id,type,card number/wallettype,expiry date/upi id,cvv/balance,amount,remarks\n"
				+ "1,card,1234567890123456,12-12-2023,123asd,10df0.0,Card Transaction\n"
				+ "2,wallet,digi-wallet,paytm@user,4fd56.00,2sdf00.0,Wallet Transaction\n";

		byte[] fileContent = csvData.getBytes();
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		csvTransactionProcessor.processTransactions(file);

		verify(cardTransactionRepository, never()).save(any(CardTransaction.class));
		verify(walletTransactionRepository, never()).save(any(WalletTransaction.class));
	}
}
