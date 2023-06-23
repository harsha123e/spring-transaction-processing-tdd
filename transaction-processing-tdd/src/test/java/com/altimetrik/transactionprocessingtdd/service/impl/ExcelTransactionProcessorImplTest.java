package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_EXCEL;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_PARAM;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
class ExcelTransactionProcessorImplTest {

	@Mock
	private CardTransactionRepository cardTransactionRepository;

	@Mock
	private WalletTransactionRepository walletTransactionRepository;

	@InjectMocks
	private ExcelTransactionProcessorImpl excelTransactionProcessor;

	@Test
	@DisplayName("If file is null - throw exception")
	void shouldThrowIllegalArgumentExceptionForInvalidFile() {
		assertThrows(IllegalArgumentException.class, () -> excelTransactionProcessor.processTransactions(null));
		verify(cardTransactionRepository, never()).save(any(CardTransaction.class));
		verify(walletTransactionRepository, never()).save(any(WalletTransaction.class));
	}

	@Test
	@DisplayName("If file only has header - don't save data")
	void shouldSkipFileIfOnlyHeaderIsPresent() throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Transactions");

			createRowData(sheet.createRow(0), "transactionId", "type", "cardNumber_walletType", "expirpyDate_upiId",
					"cvv_balance", "amount", "remarks");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byte[] fileContent = outputStream.toByteArray();
			MockMultipartFile file = new MockMultipartFile(FILE_PARAM, EXCEL_FILE_NAME, CONTENT_TYPE_EXCEL,
					fileContent);

			excelTransactionProcessor.processTransactions(file);
		}
		verify(cardTransactionRepository, never()).save(any(CardTransaction.class));
		verify(walletTransactionRepository, never()).save(any(WalletTransaction.class));
	}

	@Test
	@DisplayName("If file has valid transactions - save transactions")
	void shouldSaveAllTransactionInFile() throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Transactions");

			createRowData(sheet.createRow(0), "transactionId", "type", "cardNumber_walletType", "expirpyDate_upiId",
					"cvv_balance", "amount", "remarks");
			createRowData(sheet.createRow(1), "12321321", "card", "12321321321", "12/12/2312", "122", "231.23",
					"card transaction");
			createRowData(sheet.createRow(2), "12322321", "wallet", "digi-wallet", "12/12/2312", "3432", "231.23",
					"wallet transaction");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byte[] fileContent = outputStream.toByteArray();
			MockMultipartFile file = new MockMultipartFile(FILE_PARAM, EXCEL_FILE_NAME, CONTENT_TYPE_EXCEL,
					fileContent);

			excelTransactionProcessor.processTransactions(file);
		}
		verify(cardTransactionRepository, times(1)).save(any(CardTransaction.class));
		verify(walletTransactionRepository, times(1)).save(any(WalletTransaction.class));
	}

	private void createRowData(Row headerRow, String transactionId, String type, String cardNumber_walletType,
			String expiryDate_upiID, String cvv_balance, String amount, String remarks) {
		headerRow.createCell(0).setCellValue(transactionId);
		headerRow.createCell(1).setCellValue(type);
		headerRow.createCell(2).setCellValue(cardNumber_walletType);
		headerRow.createCell(3).setCellValue(expiryDate_upiID);
		headerRow.createCell(4).setCellValue(cvv_balance);
		headerRow.createCell(5).setCellValue(amount);
		headerRow.createCell(6).setCellValue(remarks);
	}
}
