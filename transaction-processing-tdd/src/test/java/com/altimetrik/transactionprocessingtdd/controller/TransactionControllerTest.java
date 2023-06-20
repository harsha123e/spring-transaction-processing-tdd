package com.altimetrik.transactionprocessingtdd.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@AutoConfigureMockMvc
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

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

		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("File uploaded successfully"));

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

		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("File uploaded successfully"));

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

		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("File uploaded successfully"));

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

		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@DisplayName("Empty file upload")
	public void testUploadFile_WithEmptyFile_ReturnsBadRequest() throws Exception {
		byte[] emptyFileContent = new byte[0]; // Empty file content
		MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", emptyFileContent);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("File is empty"));
	}

}
