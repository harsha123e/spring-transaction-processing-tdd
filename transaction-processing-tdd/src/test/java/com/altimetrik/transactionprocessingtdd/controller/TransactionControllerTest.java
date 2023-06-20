package com.altimetrik.transactionprocessingtdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessing.utils.TestTransactionProcessingConstants;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private TransactionController transactionController;

	@Test
	@DisplayName("Valid csv file upload")
	public void testUploadFile_WithValidCSVFile_ReturnsSuccessResponse() throws Exception {
		String filePath = TestTransactionProcessingConstants.CSV_FILE_RESOURCE;

		String controllerParam = "file";
		String originalFileName = "transactions.csv";
		String contentType = "text/csv";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TestTransactionProcessingConstants.FILE_UPLOAD_SUCCESSFUL, response.getBody());

	}

	@Test
	@DisplayName("Valid excel file upload")
	public void testUploadFile_WithValidExcelFile_ReturnsSuccessResponse() throws Exception {
		String filePath = TestTransactionProcessingConstants.EXCEL_FILE_RESOURCE;

		String controllerParam = "file";
		String originalFileName = "transactions.xlsx";
		String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TestTransactionProcessingConstants.FILE_UPLOAD_SUCCESSFUL, response.getBody());

	}

	@Test
	@DisplayName("Valid text file upload")
	public void testUploadFile_WithValidTextFile_ReturnsSuccessResponse() throws Exception {
		String filePath = TestTransactionProcessingConstants.TEXT_FILE_RESOURCE;

		String controllerParam = "file";
		String originalFileName = "transactions.txt";
		String contentType = "text/plain;charset=UTF-8";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TestTransactionProcessingConstants.FILE_UPLOAD_SUCCESSFUL, response.getBody());

	}

	@Test
	@DisplayName("Invalid file upload")
	public void testUploadFile_WithInvalidFileType_ReturnsBadRequest() throws Exception {
		String filePath = TestTransactionProcessingConstants.TEST_FILE_RESOURCES + "/invalid_file.pdf";

		String controllerParam = "file";
		String originalFileName = "invalid_file.pdf";
		String contentType = "application/pdf"; // Invalid file type
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

	@Test
	@DisplayName("Empty file upload")
	public void testUploadFile_WithEmptyFile_ReturnsBadRequest() throws Exception {
		byte[] emptyFileContent = new byte[0]; // Empty file content
		MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", emptyFileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(TestTransactionProcessingConstants.FILE_IS_EMPTY, response.getBody());

	}

	@Test
	@DisplayName("IOException when processing file")
	public void testUploadFile_WithIOException_ReturnsBadRequest() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "transactions.csv", "text/csv",
				"file content".getBytes());

		doAnswer(invocation -> {
			throw new IOException(TestTransactionProcessingConstants.FILE_PROCESSING_ERROR);
		}).when(transactionService).processAndSaveTransactions(any(MultipartFile.class));

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(TestTransactionProcessingConstants.FILE_PROCESSING_ERROR, response.getBody());
	}

}
