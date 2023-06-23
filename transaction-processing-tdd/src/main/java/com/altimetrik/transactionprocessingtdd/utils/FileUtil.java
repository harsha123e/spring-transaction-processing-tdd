package com.altimetrik.transactionprocessingtdd.utils;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

public class FileUtil {

	public static String getFileExtension(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException(INVALID_FILE_NAME);
		}
		int extensionIndex = filename.lastIndexOf('.');

		if (extensionIndex > 0 && extensionIndex < filename.length() - 1) {
			return filename.substring(extensionIndex + 1);
		} else {
			throw new IllegalArgumentException(INVALID_FILE_NAME);
		}
	}

	public static boolean isValidExtension(String extension) {
		return extension.equals(EXCEL_EXTENSION) || extension.equals(CSV_EXTENSION)
				|| extension.equals(FIXEDLENGTHFILEFORMAT_EXTENSION);
	}

	public static boolean isFileTypeMatching(String filename, String contentType) {
		String extension = getFileExtension(filename);
		if (!isValidExtension(extension)) {
			throw new IllegalArgumentException(INVALID_FILE_EXTENSION);
		}
		switch (extension) {
		case CSV_EXTENSION:
			return contentType.equals(CONTENT_TYPE_CSV);
		case EXCEL_EXTENSION:
			return contentType.equals(CONTENT_TYPE_EXCEL);
		case FIXEDLENGTHFILEFORMAT_EXTENSION:
			return contentType.equals(CONTENT_TYPE_TEXT);
		default:
			return false;
		}
	}
}
