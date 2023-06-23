package com.altimetrik.transactionprocessingtdd.utils;

import java.time.format.DateTimeFormatter;

public class TestTransactionProcessingConstants {
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static final String INVALID_FILE_NAME_ERROR = "Invalid file name";
	public static final String INVALID_FILE_EXTENSION = "Invalid file extension : ";
	public static final String INVALID_FILE_TYPE = "Invalid file type";
	public static final String EXCEL_EXTENSION = "xlsx";
	public static final String CSV_EXTENSION = "csv";
	public static final String FIXEDLENGTHFILEFORMAT_EXTENSION = "txt";
	public static final String FILE_IS_EMPTY = "File is empty";
	public static final String FILE_UPLOAD_SUCCESSFUL = "File uploaded successfully.";
	public static final String FILE_UPLOAD_FAILED = "Failed to upload file";
	public static final String CSV_FILE_NAME = "transactions.csv";
	public static final String EXCEL_FILE_NAME = "transactions.xlsx";
	public static final String TEXT_FILE_NAME = "transactions.txt";
	public static final String INVALID_FILE_NAME = "invalid_file.pdf";
	public static final String CONTENT_TYPE_CSV = "text/csv";
	public static final String CONTENT_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String CONTENT_TYPE_TEXT = "text/plain";
	public static final String CONTENT_TYPE_PDF = "application/pdf";
	public static final String FILE_PARAM = "file";
	public static final String TEST_RESOURCE = System.getProperty("user.dir") + "\\src\\test\\resources\\";
}
