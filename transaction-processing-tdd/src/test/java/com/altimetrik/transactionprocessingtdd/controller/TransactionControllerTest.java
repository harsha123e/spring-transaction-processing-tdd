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
		String filePath = System.getProperty("user.dir") + "/src/test/resources/transactions.csv";

		String controllerParam = "file";
		String originalFileName = "transactions.csv";
		String contentType = "text/csv";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("File uploaded successfully", response.getBody());

	}

	@Test
	@DisplayName("Valid excel file upload")
	public void testUploadFile_WithValidExcelFile_ReturnsSuccessResponse() throws Exception {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/transactions.xlsx";

		String controllerParam = "file";
		String originalFileName = "transactions.xlsx";
		String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("File uploaded successfully", response.getBody());

	}

	@Test
	@DisplayName("Valid text file upload")
	public void testUploadFile_WithValidTextFile_ReturnsSuccessResponse() throws Exception {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/transactions.txt";

		String controllerParam = "file";
		String originalFileName = "transactions.txt";
		String contentType = "text/plain;charset=UTF-8";
		Path path = Paths.get(filePath);
		byte[] fileContent = Files.readAllBytes(path);
		MockMultipartFile file = new MockMultipartFile(controllerParam, originalFileName, contentType, fileContent);

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("File uploaded successfully", response.getBody());

	}

	@Test
	@DisplayName("Invalid file upload")
	public void testUploadFile_WithInvalidFileType_ReturnsBadRequest() throws Exception {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/invalid_file.pdf";

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
		assertEquals("File is empty", response.getBody());

	}

	@Test
	@DisplayName("IOException when processing file")
	public void testUploadFile_WithIOException_ReturnsBadRequest() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "transactions.csv", "text/csv",
				"file content".getBytes());

		doAnswer(invocation -> {
			throw new IOException("Unable to access file");
		}).when(transactionService).processAndSaveTransactions(any(byte[].class));

		ResponseEntity<String> response = transactionController.uploadFile(file);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Unable to access file", response.getBody());
	}

}
