package com.altimetrik.transactionprocessing.utils;

import java.time.format.DateTimeFormatter;

public class TestTransactionProcessingConstants {
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static final DateTimeFormatter DATE_FORMATTER_EXCEL = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static final String INVALID_FILE_NAME = "Invalid file name";
	public static final String UNSUPPORTED_FILE_FORMAT = "Unsupported file format";
	public static final String FILE_PROCESSING_ERROR = "Error while processing the file";
	public static final String EXCEL_EXTENSION = ".xlsx";
	public static final String CSV_EXTENSION = ".csv";
	public static final String FIXEDLENGTHFILEFORMAT_EXTENSION = ".txt";
	public static final String TRANSACTIONTYPE_CARD = "card";
	public static final String TRANSACTIONTYPE_WALLET = "wallet";
	public static final String CSV_DELIMITER = ",";
	public static final String FILE_IS_EMPTY = "File is empty";
	public static final String FILE_UPLOAD_SUCCESSFUL = "File uploaded successfully.";
	public static final String FILE_UPLOAD_FAILED = "Failed to upload file";
	public static final String TEST_FILE_RESOURCES = System.getProperty("user.dir") + "/src/test/resources";
	public static final String CSV_FILE_RESOURCE = TEST_FILE_RESOURCES + "/transactions.csv";
	public static final String EXCEL_FILE_RESOURCE = TEST_FILE_RESOURCES + "/transactions.xlsx";
	public static final String TEXT_FILE_RESOURCE = TEST_FILE_RESOURCES + "/transactions.txt";

}
