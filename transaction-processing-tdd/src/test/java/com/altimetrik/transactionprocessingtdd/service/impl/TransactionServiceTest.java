package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FILE_IS_EMPTY;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.INVALID_FILE_NAME_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	public void testProcessAndSaveTransactions_WhenFileIsEmpty_ThrowsIllegalArgumentException() {

		MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processAndSaveTransactions(file);
		});

		assertEquals(FILE_IS_EMPTY, exception.getMessage());
	}

	@Test
	public void testProcessAndSaveTransactions_WhenFileNameIsNull_ThrowsIllegalArgumentException() {

		MultipartFile file = mock(MultipartFile.class);
		when(file.getOriginalFilename()).thenReturn(null);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processAndSaveTransactions(file);
		});

		assertEquals(INVALID_FILE_NAME_ERROR, exception.getMessage());
	}

	@Test
	public void testProcessAndSaveTransactions_ValidFile_InvokesTransactionProcessor() throws IOException {
		MultipartFile file = mock(MultipartFile.class);
		when(file.getOriginalFilename()).thenReturn("file.csv");
		TransactionProcessor mockProcessor = mock(TransactionProcessor.class);
		when(transactionProcessorFactory.getTransactionProcessor("csv")).thenReturn(mockProcessor);

		transactionService.processAndSaveTransactions(file);

		verify(transactionProcessorFactory).getTransactionProcessor(eq("csv"));
		verify(mockProcessor).processTransactions(eq(file));
	}
}
