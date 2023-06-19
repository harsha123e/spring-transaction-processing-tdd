package com.altimetrik.transactionprocessingtdd.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
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

}
