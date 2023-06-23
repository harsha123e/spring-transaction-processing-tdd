package com.altimetrik.transactionprocessingtdd.endtoend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.altimetrik.transactionprocessingtdd.controller.TransactionController;
import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_CSV;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_PARAM;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

@SpringBootTest
@Transactional
class CSVFileUploadEndToEndTest {

	@Autowired
	private TransactionController controller;

	@Autowired
	private CardTransactionRepository cardRepository;

	@Autowired
	private WalletTransactionRepository walletRepository;

	@Test
	void csvFileDataIsPersistedForValidInputCsvFileInputAndReturnsSuccessResponse() {

		String csvData = "transaction id,type,card number/wallettype,expiry date/upi id,cvv/balance,amount,remarks\n"
				+ "1,card,1234567890123456,12-12-2023,123,100.0,Card Transaction\n"
				+ "2,wallet,digi-wallet,paytm@user,456.00,200.0,Wallet Transaction\n";

		byte[] fileContent = csvData.getBytes();
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		ResponseEntity<String> response = controller.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(FILE_UPLOAD_SUCCESSFUL, response.getBody());

		CardTransaction cardTransactionDetails = cardRepository.getById("1");
		assertEquals("1", cardTransactionDetails.getTransactionId());
		assertEquals("1234567890123456", cardTransactionDetails.getCardNumber());
		assertEquals(123, cardTransactionDetails.getCvv());
		assertEquals(100.0, cardTransactionDetails.getAmount());
		assertEquals("Card Transaction", cardTransactionDetails.getRemarks());

		WalletTransaction walletTransactionDetails = walletRepository.getById("2");
		assertEquals("2", walletTransactionDetails.getTransactionId());
		assertEquals("digi-wallet", walletTransactionDetails.getWalletType());
		assertEquals("paytm@user", walletTransactionDetails.getUpiId());
		assertEquals(200.0, walletTransactionDetails.getAmount());
		assertEquals("Wallet Transaction", walletTransactionDetails.getRemarks());

	}

}
