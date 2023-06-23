package com.altimetrik.transactionprocessingtdd.utils;

import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_CSV;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_EXCEL;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_PDF;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CONTENT_TYPE_TEXT;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.CSV_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.EXCEL_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.FIXEDLENGTHFILEFORMAT_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.INVALID_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TestTransactionProcessingConstants.TEXT_FILE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class FileUtilTest {

	@DisplayName("For valid file name and type - return true")
	@ParameterizedTest(name = "Valid file type {2} - returns true")
	@CsvSource({ CSV_FILE_NAME + ", " + CONTENT_TYPE_CSV + ", " + CSV_EXTENSION,
			EXCEL_FILE_NAME + ", " + CONTENT_TYPE_EXCEL + ", " + EXCEL_EXTENSION,
			TEXT_FILE_NAME + ", " + CONTENT_TYPE_TEXT + ", " + FIXEDLENGTHFILEFORMAT_EXTENSION, })
	void shouldReturnTrueForValidFileType(String fileName, String contentType, String fileExtension) {
		assertTrue(FileUtil.isFileTypeMatching(fileName, contentType));
	}

	@DisplayName("For invalid file name and type - return false")
	@ParameterizedTest(name = "Invalid file type {2} - returns false")
	@CsvSource({ CSV_FILE_NAME + ", " + CONTENT_TYPE_EXCEL + ", " + CSV_EXTENSION,
			EXCEL_FILE_NAME + ", " + CONTENT_TYPE_CSV + ", " + EXCEL_EXTENSION,
			TEXT_FILE_NAME + ", " + CONTENT_TYPE_CSV + ", " + FIXEDLENGTHFILEFORMAT_EXTENSION, })
	void shouldReturnFalseForInValidFileType(String fileName, String contentType, String fileExtension) {
		assertFalse(FileUtil.isFileTypeMatching(fileName, contentType));
	}

	@DisplayName("For invalid file name - throw exception")
	@ParameterizedTest(name = "Invalid file name {2} - throws exception")
	@CsvSource({ INVALID_FILE_NAME + ", " + CONTENT_TYPE_PDF + ", " + "pdf", })
	void shouldThrowExceptionForInValidFileName(String fileName, String contentType, String fileExtension) {
		assertThrows(IllegalArgumentException.class, () -> FileUtil.isFileTypeMatching(fileName, contentType));
	}

	@DisplayName("For file name - return extension")
	@ParameterizedTest(name = "Expected file extension {0}")
	@MethodSource("fileNameProvider")
	void shouldReturnExtensionStringForGivenValidInput(String fileExtension, String fileName) {
		assertEquals(fileExtension, FileUtil.getFileExtension(fileName));
	}

	private static Stream<Arguments> fileNameProvider() {
		return Stream.of(Arguments.of(CSV_EXTENSION, CSV_FILE_NAME), Arguments.of(EXCEL_EXTENSION, EXCEL_FILE_NAME),
				Arguments.of(FIXEDLENGTHFILEFORMAT_EXTENSION, TEXT_FILE_NAME), Arguments.of("pdf", "asdb.sdf.pdf"));
	}

	@DisplayName("For invalid file name - throw exception")
	@ParameterizedTest(name = "file name {0}")
	@NullSource
	@EmptySource
	@ValueSource(strings = { ".", ".....", "  ", "asdbsdf" })
	void shouldThrowExceptionForInvalidInput(String fileName) {
		assertThrows(IllegalArgumentException.class, () -> FileUtil.getFileExtension(fileName));
	}

	@DisplayName("For valid extension - return true")
	@ParameterizedTest(name = "For extension {0}")
	@ValueSource(strings = { "csv", "xlsx", "txt" })
	void shouldReturnTrueForValidFileExtension(String fileExtension) {
		assertTrue(FileUtil.isValidExtension(fileExtension));
	}

	@DisplayName("For invalid extension - return false")
	@ParameterizedTest(name = "For extension {0}")
	@ValueSource(strings = { "pdf", "mp3", "jpg" })
	void shouldReturnFalseForInvalidFileExtension(String fileExtension) {
		assertFalse(FileUtil.isValidExtension(fileExtension));
	}
}
