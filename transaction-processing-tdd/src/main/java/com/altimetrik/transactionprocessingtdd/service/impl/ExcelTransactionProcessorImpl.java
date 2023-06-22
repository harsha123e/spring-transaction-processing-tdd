package com.altimetrik.transactionprocessingtdd.service.impl;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.DATE_FORMATTER_EXCEL;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.TRANSACTIONTYPE_CARD;
import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.TRANSACTIONTYPE_WALLET;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;
import com.altimetrik.transactionprocessingtdd.repository.CardTransactionRepository;
import com.altimetrik.transactionprocessingtdd.repository.WalletTransactionRepository;
import com.altimetrik.transactionprocessingtdd.service.TransactionProcessor;

@Service
@Qualifier("excelTransactionProcessor")
public class ExcelTransactionProcessorImpl implements TransactionProcessor {

	private CardTransactionRepository cardTransactionRepository;
	private WalletTransactionRepository walletTransactionRepository;

	@Autowired
	public ExcelTransactionProcessorImpl(CardTransactionRepository cardTransactionRepository,
			WalletTransactionRepository walletTransactionRepository) {
		this.cardTransactionRepository = cardTransactionRepository;
		this.walletTransactionRepository = walletTransactionRepository;
	}

	@Override
	public void processTransactions(MultipartFile file) throws IOException {
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
}
