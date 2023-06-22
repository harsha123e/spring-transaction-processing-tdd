package com.altimetrik.transactionprocessingtdd.controller;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.altimetrik.transactionprocessingtdd.exception.EmptyFileException;
import com.altimetrik.transactionprocessingtdd.exception.UnsupportedFileFormatException;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private TransactionController transactionController;

	@Test
	@DisplayName("Valid csv file upload")
	public void testUploadFile_WithValidCSVFile_ReturnsSuccessResponse() throws IOException {

		byte[] fileContent = Files.readAllBytes(Paths.get(CSV_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(FILE_UPLOAD_SUCCESSFUL, response.getBody());

	}

	@Test
	@DisplayName("Valid excel file upload")
	public void testUploadFile_WithValidExcelFile_ReturnsSuccessResponse() throws Exception {

		byte[] fileContent = Files.readAllBytes(Paths.get(EXCEL_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, EXCEL_FILE_NAME, CONTENT_TYPE_EXCEL, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(FILE_UPLOAD_SUCCESSFUL, response.getBody());

	}

	@Test
	@DisplayName("Valid text file upload")
	public void testUploadFile_WithValidTextFile_ReturnsSuccessResponse() throws Exception {

		byte[] fileContent = Files.readAllBytes(Paths.get(TEXT_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, TEXT_FILE_NAME, CONTENT_TYPE_TXT, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(FILE_UPLOAD_SUCCESSFUL, response.getBody());

	}

	@Test
	@DisplayName("Invalid file upload")
	public void testUploadFile_WithInvalidFileType_ReturnsBadRequest() throws Exception {

		byte[] fileContent = Files.readAllBytes(Paths.get(INVALID_FILE_RESOURCE));
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, INVALID_FILE_NAME, CONTENT_TYPE_PDF, fileContent);
		doThrow(new UnsupportedFileFormatException(UNSUPPORTED_FILE_FORMAT)).when(transactionService)
				.processAndSaveTransactions(file);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(UNSUPPORTED_FILE_FORMAT, response.getBody());

	}

	@Test
	@DisplayName("Empty file upload")
	public void testUploadFile_WithEmptyFile_ReturnsBadRequest() throws Exception {

		byte[] emptyFileContent = new byte[0]; // Empty file content
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, emptyFileContent);
		doThrow(new EmptyFileException(FILE_IS_EMPTY)).when(transactionService).processAndSaveTransactions(file);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(FILE_IS_EMPTY, response.getBody());

	}

	@Test
	@DisplayName("IOException when processing file")
	public void testUploadFile_WithIOException_ReturnsBadRequest() throws Exception {

		byte[] fileContent = "file content".getBytes();
		MockMultipartFile file = new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent);
		doThrow(new IOException(FILE_PROCESSING_ERROR)).when(transactionService).processAndSaveTransactions(file);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(FILE_PROCESSING_ERROR, response.getBody());
	}

}
