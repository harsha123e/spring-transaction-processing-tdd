package com.altimetrik.transactionprocessingtdd.config;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.altimetrik.transactionprocessingtdd.model.CardTransaction;
import com.altimetrik.transactionprocessingtdd.model.WalletTransaction;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();

		mapper.addMappings(new PropertyMap<String[], CardTransaction>() {
			@Override
			protected void configure() {
				map().setTransactionId(source[0]);
				map().setCardNumber(source[2]);
				map().setExpiryDate(LocalDate.parse(source[3], DATE_FORMATTER));
				map().setCvv(Integer.parseInt(source[4]));
				map().setAmount(Double.parseDouble(source[5]));
				map().setRemarks(source[6]);
			}
		});

		mapper.addMappings(new PropertyMap<String[], WalletTransaction>() {

			@Override
			protected void configure() {
				map().setTransactionId(source[0]);
				map().setWalletType(source[2]);
				map().setUpiId(source[3]);
				map().setBalance(Double.parseDouble(source[4]));
				map().setAmount(Double.parseDouble(source[5]));
				map().setRemarks(source[6]);
			}
		});

		mapper.addMappings(new PropertyMap<Row, CardTransaction>() {
			@Override
			protected void configure() {
				map().setTransactionId(getCellValue(source, 0));
				map().setCardNumber(getCellValue(source, 2));
				map().setExpiryDate(LocalDate.parse(getCellValue(source, 3), DATE_FORMATTER_EXCEL));
				map().setCvv(Integer.parseInt(getCellValue(source, 4)));
				map().setAmount(Double.parseDouble(getCellValue(source, 5)));
				map().setRemarks(getCellValue(source, 6));
			}
		});

		mapper.addMappings(new PropertyMap<Row, WalletTransaction>() {
			@Override
			protected void configure() {
				map().setTransactionId(getCellValue(source, 0));
				map().setWalletType(getCellValue(source, 2));
				map().setUpiId(getCellValue(source, 3));
				map().setBalance(Double.parseDouble(getCellValue(source, 4)));
				map().setAmount(Double.parseDouble(getCellValue(source, 5)));
				map().setRemarks(getCellValue(source, 6));
			}
		});

		return mapper;
	}

	private String getCellValue(Row row, int index) {
		DataFormatter dataFormatter = new DataFormatter();
		Cell cell = row.getCell(index);
		return dataFormatter.formatCellValue(cell);
	}
}
