package com.altimetrik.transactionprocessingtdd.utils;

import java.time.format.DateTimeFormatter;

public class TransactionProcessingConstants {
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static final DateTimeFormatter DATE_FORMATTER_EXCEL = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static final String INVALID_FILE_NAME = "Invalid file name";
	public static final String INVALID_FILE_TYPE = "Invalid file type";
	public static final String INVALID_FILE_EXTENSION = "Invalid file extension : ";
	public static final String FILE_PROCESSING_ERROR = "Error while processing the file";
	public static final String NO_SUCH_TRANSACTION_PROCESSOR = "No such transaction processor exists for given extension : ";
	public static final String EXCEL_EXTENSION = "xlsx";
	public static final String CSV_EXTENSION = "csv";
	public static final String FIXEDLENGTHFILEFORMAT_EXTENSION = "txt";
	public static final String CONTENT_TYPE_CSV = "text/csv";
	public static final String CONTENT_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String CONTENT_TYPE_TEXT = "text/plain";
	public static final String TRANSACTIONTYPE_CARD = "card";
	public static final String TRANSACTIONTYPE_WALLET = "wallet";
	public static final String CSV_DELIMITER = ",";
	public static final String FILE_IS_EMPTY = "File is empty";
	public static final String FILE_UPLOAD_SUCCESSFUL = "File uploaded successfully.";
	public static final String FILE_UPLOAD_FAILED = "Failed to upload file";

}
