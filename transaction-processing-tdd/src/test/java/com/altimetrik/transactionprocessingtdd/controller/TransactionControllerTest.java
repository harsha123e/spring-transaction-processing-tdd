package com.altimetrik.transactionprocessingtdd.controller;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_CSV;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_EXCEL;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_TEXT;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_IS_EMPTY;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_PARAM;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_UPLOAD_SUCCESSFUL;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FIXEDLENGTHFILEFORMAT_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.INVALID_FILE_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.INVALID_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.INVALID_FILE_NAME_ERROR;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.INVALID_FILE_TYPE;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.TEXT_FILE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private TransactionController transactionController;

	private byte[] fileContent = "File content".getBytes();

	@Test
	@DisplayName("Empty file upload - returns bad request")
	public void shouldReturnBadRequestForEmptyFile() throws Exception {

		byte[] emptyFileContent = new byte[0]; // Empty file content
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, emptyFileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		verify(transactionService, times(0)).processAndSaveTransactions(any(), any());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(FILE_IS_EMPTY, response.getBody());
	}

	@Test
	@DisplayName("Null or empty filename - returns bad request")
	public void shouldReturnBadRequestForNullOrBlankFileName() throws Exception {

		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, "", CONTENT_TYPE_CSV, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		verify(transactionService, times(0)).processAndSaveTransactions(any(), any());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(INVALID_FILE_NAME_ERROR, response.getBody());
	}

	@Test
	@DisplayName("Invalid file extension - returns bad request")
	public void shouldReturnBadRequestForInvalidFileName() throws Exception {

		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, INVALID_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		verify(transactionService, times(0)).processAndSaveTransactions(any(), any());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains(INVALID_FILE_EXTENSION));
	}

	@ParameterizedTest(name = "Valid {2} file upload - returns success request")
	@CsvSource({ CSV_FILE_NAME + ", " + CONTENT_TYPE_CSV + ", " + CSV_EXTENSION,
			EXCEL_FILE_NAME + ", " + CONTENT_TYPE_EXCEL + ", " + EXCEL_EXTENSION,
			TEXT_FILE_NAME + ", " + CONTENT_TYPE_TEXT + ", " + FIXEDLENGTHFILEFORMAT_EXTENSION, })
	public void shouldReturnSuccessReponseForValidFiles(String fileName, String contentType, String fileExtension)
			throws IOException {

		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, fileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		verify(transactionService, times(1)).processAndSaveTransactions(any(), any());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(FILE_UPLOAD_SUCCESSFUL, response.getBody());
	}

	@ParameterizedTest(name = "Valid {2} file upload - returns success request")
	@CsvSource({ CSV_FILE_NAME + ", " + CONTENT_TYPE_EXCEL + ", " + CSV_EXTENSION,
			EXCEL_FILE_NAME + ", " + CONTENT_TYPE_CSV + ", " + EXCEL_EXTENSION,
			TEXT_FILE_NAME + ", " + CONTENT_TYPE_CSV + ", " + FIXEDLENGTHFILEFORMAT_EXTENSION, })
	public void shouldReturnBadReponseForInValidFileType(String fileName, String contentType, String fileExtension)
			throws IOException {

		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, fileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		verify(transactionService, times(0)).processAndSaveTransactions(any(), any());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(INVALID_FILE_TYPE, response.getBody());
	}

	@Test
	@DisplayName("Service method throws exception - returns bad request")
	public void shouldReturnBadRequestWhenServiceMethodThrowsIOException() throws Exception {

		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);
		doThrow(IOException.class).when(transactionService).processAndSaveTransactions(any(), any());

		ResponseEntity<String> response = transactionController.uploadFile(file);

		response = transactionController.uploadFile(file);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

}
