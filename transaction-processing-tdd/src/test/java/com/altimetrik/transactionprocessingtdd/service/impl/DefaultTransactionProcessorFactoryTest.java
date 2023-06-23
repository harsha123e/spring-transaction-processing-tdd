package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FIXEDLENGTHFILEFORMAT_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import com.altimetrik.transactionprocessingtdd.service.TransactionProcessor;

@ExtendWith(MockitoExtension.class)
class DefaultTransactionProcessorFactoryTest {

	@Mock
	@Qualifier("csvTransactionProcessor")
	private TransactionProcessor csvTransactionProcessor;

	@Mock
	@Qualifier("excelTransactionProcessor")
	private TransactionProcessor excelTransactionProcessor;

	@Mock
	@Qualifier("fixedFileLengthTransactionProcessor")
	private TransactionProcessor fixedFileLengthTransactionProcessor;

	@InjectMocks
	DefaultTransactionProcessorFactory factory;

	@DisplayName("For valid file extension - return TransactionProcessor")
	@ParameterizedTest(name = "For valid {0} file extension")
	@CsvSource({ CSV_EXTENSION, EXCEL_EXTENSION, FIXEDLENGTHFILEFORMAT_EXTENSION })
	public void shouldReturnValidTransactionProcessorForValidFileExtension(String fileExtension) {

		TransactionProcessor actualProcessor = factory.getTransactionProcessor(fileExtension);
		assertThat(actualProcessor).isInstanceOf(TransactionProcessor.class);
	}

	@DisplayName("For invalid file extension - throw exception")
	@ParameterizedTest(name = "For {0} input")
	@NullSource
	@EmptySource
	@ValueSource(strings = { "pdf", "    " })
	public void shouldThrowIllegalArgumentExceptionForInvalidFileExtension(String fileExtension) {
		assertThrows(IllegalArgumentException.class, () -> factory.getTransactionProcessor(fileExtension));
	}

}
