package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.*;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_PARAM;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.service.TransactionProcessor;
import com.altimetrik.transactionprocessingtdd.service.TransactionProcessorFactory;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionProcessorFactory transactionProcessorFactory;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Test
	@DisplayName("If file is null - throw exception")
	public void shouldThrowIllegalArgumentExceptionForNullFile() {
		assertThrows(IllegalArgumentException.class, () -> transactionService.processAndSaveTransactions(null, "csv"));
	}

	@Test
	@DisplayName("If invalid file extension - throw exception")
	public void shouldThrowIllegalArgumentExceptionForInvalidFileExtension() {
		assertThrows(IllegalArgumentException.class,
				() -> transactionService.processAndSaveTransactions(mock(MultipartFile.class), null));
		assertThrows(IllegalArgumentException.class,
				() -> transactionService.processAndSaveTransactions(mock(MultipartFile.class), ""));
		assertThrows(IllegalArgumentException.class,
				() -> transactionService.processAndSaveTransactions(mock(MultipartFile.class), "   "));
		assertThrows(IllegalArgumentException.class,
				() -> transactionService.processAndSaveTransactions(mock(MultipartFile.class), "pdf"));
	}

	@Test
	@DisplayName("If TransactionProcessorFactory is null - throw exception")
	public void shouldThrowIllegalArgumentExceptionForNullTransactionProcessorFactory() {
		String fileExtension = "csv";
		when(transactionProcessorFactory.getTransactionProcessor(fileExtension)).thenReturn(null);
		assertThrows(IllegalArgumentException.class,
				() -> transactionService.processAndSaveTransactions(mock(MultipartFile.class), fileExtension));
	}

	@ParameterizedTest(name = "For {1} input")
	@DisplayName("For valid input - processTransactions is invoked")
	@MethodSource("validInputProvider")
	public void shouldInvokeProcessTransactionForValidInput(MockMultipartFile file, String fileExtension) throws IOException {

		when(transactionProcessorFactory.getTransactionProcessor(fileExtension))
				.thenReturn(mock(TransactionProcessor.class));

		transactionService.processAndSaveTransactions(file, fileExtension);

		verify(transactionProcessorFactory.getTransactionProcessor(fileExtension)).processTransactions(file);
	}

	private static Stream<Arguments> validInputProvider() {
		byte[] fileContent = "file content".getBytes();
		return Stream.of(
				Arguments.of(new MockMultipartFile(FILE_PARAM, CSV_FILE_NAME, CONTENT_TYPE_CSV, fileContent),
						CSV_EXTENSION),
				Arguments.of(new MockMultipartFile(FILE_PARAM, EXCEL_FILE_NAME, CONTENT_TYPE_EXCEL, fileContent),
						EXCEL_EXTENSION));
	}

}
