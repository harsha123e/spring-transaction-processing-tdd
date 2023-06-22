package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.CSV_DELIMITER;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.CSV_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.DATE_FORMATTER;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.DATE_FORMATTER_EXCEL;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.EXCEL_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.FILE_IS_EMPTY;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.FIXEDLENGTHFILEFORMAT_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.INVALID_FILE_EXTENSION;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.INVALID_FILE_NAME;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.TRANSACTIONTYPE_CARD;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.TRANSACTIONTYPE_WALLET;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private CardTransactionRepository cardTransactionRepository;
	private WalletTransactionRepository walletTransactionRepository;

	@Autowired
	public TransactionServiceImpl(CardTransactionRepository cardTransactionRepository,
			WalletTransactionRepository walletTransactionRepository) {
		this.cardTransactionRepository = cardTransactionRepository;
		this.walletTransactionRepository = walletTransactionRepository;
	}

	@Override
	public void processAndSaveTransactions(MultipartFile file) throws IOException {

		if (file.isEmpty()) {
			throw new IllegalArgumentException(FILE_IS_EMPTY);
		}

		String filename = file.getOriginalFilename();
		if (filename == null) {
			throw new IllegalArgumentException(INVALID_FILE_NAME);
		}

		String fileExtension = getFileExtension(filename);
		switch (fileExtension) {
		case EXCEL_EXTENSION:
			processExcelFile(file);
			break;
		case CSV_EXTENSION:
			processCsvFile(file);
			break;
		case FIXEDLENGTHFILEFORMAT_EXTENSION:
			processFixedLengthFile(file);
			break;
		default:
			throw new IllegalArgumentException(INVALID_FILE_EXTENSION);
		}

	}

	private String getFileExtension(String filename) {

		int extensionIndex = filename.lastIndexOf('.');
		return (extensionIndex >= 0 && extensionIndex < filename.length() - 1) ? filename.substring(extensionIndex + 1)
				: "";
	}

	private void processExcelFile(MultipartFile file) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming there is only one sheet
			Stream<Row> rowStream = StreamSupport.stream(sheet.spliterator(), false);

			rowStream.skip(1).forEach(this::saveTransactionFromRow);

		}
	}

	private void saveTransactionFromRow(Row row) {
		String transactionId = getCellValue(row, 0);
		String transactionType = getCellValue(row, 1);
		double amount = Double.parseDouble(getCellValue(row, 5));
		String remarks = getCellValue(row, 6);

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transactionType)) {
			String cardNumber = getCellValue(row, 2);
			LocalDate expiryDate = LocalDate.parse(getCellValue(row, 3), DATE_FORMATTER_EXCEL);
			int cvv = Integer.parseInt(getCellValue(row, 4));

			cardTransactionRepository
					.save(new CardTransaction(transactionId, cardNumber, expiryDate, cvv, amount, remarks));
		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transactionType)) {
			String walletType = getCellValue(row, 2);
			String upiId = getCellValue(row, 3);
			double balance = Double.parseDouble(getCellValue(row, 4));

			walletTransactionRepository
					.save(new WalletTransaction(transactionId, walletType, upiId, balance, amount, remarks));
		}
	}

	private String getCellValue(Row row, int index) {
		DataFormatter dataFormatter = new DataFormatter();
		Cell cell = row.getCell(index);
		return dataFormatter.formatCellValue(cell);
	}

	private void processCsvFile(MultipartFile file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			reader.lines().skip(1).forEach(this::saveTransactionFromCSV);

		}
	}

	private void saveTransactionFromCSV(String record) {

		String[] fields = record.split(CSV_DELIMITER);

		String transactionId = fields[0];
		String transactionType = fields[1];
		double amount = Double.parseDouble(fields[5]);
		String remarks = fields[6];

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transactionType)) {

			String cardNumber = fields[2];
			LocalDate expiryDate = LocalDate.parse(fields[3], DATE_FORMATTER);
			int cvv = Integer.parseInt(fields[4]);

			cardTransactionRepository
					.save(new CardTransaction(transactionId, cardNumber, expiryDate, cvv, amount, remarks));
		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transactionType)) {

			String walletType = fields[2];
			String upiId = fields[3];
			double balance = Double.parseDouble(fields[4]);

			walletTransactionRepository
					.save(new WalletTransaction(transactionId, walletType, upiId, balance, amount, remarks));
		}
	}

	private void processFixedLengthFile(MultipartFile file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			reader.lines().skip(1).forEach(this::saveTransactionFromFixedLengthFile);

		}
	}

	private void saveTransactionFromFixedLengthFile(String record) {
		String transactionId = record.substring(0, 10).trim();
		String transactionType = record.substring(11, 26).trim();
		double amount = Double.parseDouble(record.substring(55, 67).trim());
		String remarks = record.substring(67).trim();

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transactionType)) {
			String cardNumber = record.substring(26, 39).trim();
			int cvv = Integer.parseInt(record.substring(40, 43).trim());
			LocalDate expiryDate = LocalDate.parse(record.substring(44, 54).trim(), DATE_FORMATTER);

			cardTransactionRepository
					.save(new CardTransaction(transactionId, cardNumber, expiryDate, cvv, amount, remarks));
		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transactionType)) {
			String walletType = record.substring(26, 39).trim();
			String upiId = record.substring(40, 43).trim();
			double balance = Double.parseDouble(record.substring(44, 54).trim());

			walletTransactionRepository
					.save(new WalletTransaction(transactionId, walletType, upiId, balance, amount, remarks));
		}
	}

}
