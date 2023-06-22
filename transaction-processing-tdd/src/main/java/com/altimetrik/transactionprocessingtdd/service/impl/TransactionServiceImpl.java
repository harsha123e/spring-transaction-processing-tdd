package com.altimetrik.transactionprocessingtdd.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.dto.TransactionDto;
import com.altimetrik.transactionprocessingtdd.exception.FileProcessingException;
import com.altimetrik.transactionprocessingtdd.exception.InvalidFileNameException;
import com.altimetrik.transactionprocessingtdd.exception.UnsupportedFileFormatException;
import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private CardTransactionRepository cardTransactionRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	@Override
	public void processAndSaveTransactions(MultipartFile file)
			throws IOException, InvalidFileNameException, UnsupportedFileFormatException, FileProcessingException {
		String filename = file.getOriginalFilename();
		if (filename == null) {
			throw new InvalidFileNameException(INVALID_FILE_NAME);
		}

		try {
			if (filename.endsWith(EXCEL_EXTENSION)) {
				processExcelFile(file);
			} else if (filename.endsWith(CSV_EXTENSION)) {
				processCsvFile(file);
			} else if (filename.endsWith(FIXEDLENGTHFILEFORMAT_EXTENSION)) {
				processFixedLengthFile(file);
			} else {
				throw new UnsupportedFileFormatException(UNSUPPORTED_FILE_FORMAT);
			}
		} catch (FileProcessingException e) {
			throw new FileProcessingException(FILE_PROCESSING_ERROR + e.getMessage());
		}

	}

	private void processExcelFile(MultipartFile file) throws IOException, FileProcessingException {
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming there is only one sheet

			boolean skipHeader = true; // Flag to skip the first row (header)

			for (Row row : sheet) {
				if (skipHeader) {
					skipHeader = false;
					continue; // Skip the first row
				}

				TransactionDto transaction = getTransactionFromRow(row);
				saveTransaction(transaction);
			}
		} catch (IOException e) {
			throw new FileProcessingException();
		}
	}

	private void processCsvFile(MultipartFile file) throws IOException, FileProcessingException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			List<String> lines = reader.lines().skip(1).collect(Collectors.toList()); // Skip the header line

			for (String line : lines) {
				String[] fields = line.split(CSV_DELIMITER); // Assuming fields are
																// comma-separated
				TransactionDto transaction = getTransactionFromFields(fields);
				saveTransaction(transaction);
			}
		} catch (IOException e) {
			throw new FileProcessingException(FILE_PROCESSING_ERROR + e.getMessage());
		}
	}

	private void processFixedLengthFile(MultipartFile file) throws IOException, FileProcessingException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			List<String> lines = reader.lines().skip(1).collect(Collectors.toList()); // Skip the header line

			for (String line : lines) {
				TransactionDto transaction = getTransactionFromFixedLengthFile(line);
				saveTransaction(transaction);
			}
		} catch (IOException e) {
			throw new FileProcessingException(FILE_PROCESSING_ERROR + e.getMessage());
		}
	}

	private TransactionDto getTransactionFromRow(Row row) {

		DataFormatter dataFormatter = new DataFormatter();
		TransactionDto transaction = new TransactionDto();

		String transactionId = dataFormatter.formatCellValue(row.getCell(0));
		String transactionType = dataFormatter.formatCellValue(row.getCell(1));
		double amount = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(5)));
		String remarks = dataFormatter.formatCellValue(row.getCell(6));

		transaction.setTransactionId(transactionId);
		transaction.setTransactionType(transactionType);
		transaction.setAmount(amount);
		transaction.setRemarks(remarks);

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transaction.getTransactionType())) {

			String cardNumber = dataFormatter.formatCellValue(row.getCell(2));
			LocalDate expiryDate = LocalDate.parse(dataFormatter.formatCellValue(row.getCell(3)), DATE_FORMATTER_EXCEL);
			int cvv = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(4)));

			transaction.setCardNumber(cardNumber);
			transaction.setExpiryDate(expiryDate);
			transaction.setCvv(cvv);
		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transaction.getTransactionType())) {

			String walletType = dataFormatter.formatCellValue(row.getCell(2));
			String upiId = dataFormatter.formatCellValue(row.getCell(3));
			double balance = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(4)));

			transaction.setWalletType(walletType);
			transaction.setUpiId(upiId);
			transaction.setBalance(balance);
		}

		return transaction;
	}

	private TransactionDto getTransactionFromFields(String[] fields) {

		TransactionDto transaction = new TransactionDto();

		String transactionId = fields[0];
		String transactionType = fields[1];
		double amount = Double.parseDouble(fields[5]);
		String remarks = fields[6];

		transaction.setTransactionId(transactionId);
		transaction.setTransactionType(transactionType);
		transaction.setAmount(amount);
		transaction.setRemarks(remarks);

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transaction.getTransactionType())) {

			String cardNumber = fields[2];
			LocalDate expiryDate = LocalDate.parse(fields[3], DATE_FORMATTER);
			int cvv = Integer.parseInt(fields[4]);

			transaction.setCardNumber(cardNumber);
			transaction.setExpiryDate(expiryDate);
			transaction.setCvv(cvv);

		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transaction.getTransactionType())) {

			String walletType = fields[2];
			String upiId = fields[3];
			double balance = Double.parseDouble(fields[4]);

			transaction.setWalletType(walletType);
			transaction.setUpiId(upiId);
			transaction.setBalance(balance);

		}

		return transaction;
	}

	private TransactionDto getTransactionFromFixedLengthFile(String line) {
		TransactionDto transaction = new TransactionDto();

		String transactionId = line.substring(0, 10).trim();
		String transactionType = line.substring(11, 26).trim();
		double amount = Double.parseDouble(line.substring(55, 67).trim());
		String remarks = line.substring(67).trim();

		transaction.setTransactionId(transactionId);
		transaction.setTransactionType(transactionType);
		transaction.setAmount(amount);
		transaction.setRemarks(remarks);

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transaction.getTransactionType())) {

			String cardNumber = line.substring(26, 39).trim();
			int cvv = Integer.parseInt(line.substring(40, 43).trim());
			LocalDate expiryDate = LocalDate.parse(line.substring(44, 54).trim(), DATE_FORMATTER);

			transaction.setCardNumber(cardNumber);
			transaction.setExpiryDate(expiryDate);
			transaction.setCvv(cvv);

		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transaction.getTransactionType())) {

			String walletType = line.substring(26, 39).trim();
			String upiId = line.substring(40, 43).trim();
			double balance = Double.parseDouble(line.substring(44, 54).trim());

			transaction.setWalletType(walletType);
			transaction.setUpiId(upiId);
			transaction.setBalance(balance);

		}

		return transaction;
	}

	private void saveTransaction(TransactionDto transaction) {

		if (TRANSACTIONTYPE_CARD.equalsIgnoreCase(transaction.getTransactionType())) {

			CardTransaction newTransaction = new CardTransaction(transaction.getTransactionId(),
					transaction.getCardNumber(), transaction.getExpiryDate(), transaction.getCvv(),
					transaction.getAmount(), transaction.getRemarks());
			cardTransactionRepository.save(newTransaction);

		} else if (TRANSACTIONTYPE_WALLET.equalsIgnoreCase(transaction.getTransactionType())) {

			WalletTransaction newTransaction = new WalletTransaction(transaction.getTransactionId(),
					transaction.getWalletType(), transaction.getUpiId(), transaction.getBalance(),
					transaction.getAmount(), transaction.getRemarks());
			walletTransactionRepository.save(newTransaction);
		}
		System.out.println(transaction);
	}

}
