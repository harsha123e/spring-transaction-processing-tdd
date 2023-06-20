package com.altimetrik.transactionprocessingtdd.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessing.utils.TransactionProcessingConstants;
import com.altimetrik.transactionprocessingtdd.exception.FileProcessingException;
import com.altimetrik.transactionprocessingtdd.exception.InvalidFileNameException;
import com.altimetrik.transactionprocessingtdd.exception.UnsupportedFileFormatException;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(TransactionProcessingConstants.FILE_IS_EMPTY);
		}

		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null && !isValidFileType(originalFilename)) {
			return ResponseEntity.badRequest().body(TransactionProcessingConstants.UNSUPPORTED_FILE_FORMAT);
		}

		try {
			transactionService.processAndSaveTransactions(file);
		} catch (InvalidFileNameException e) {
			return ResponseEntity.badRequest().body(TransactionProcessingConstants.UNSUPPORTED_FILE_FORMAT);
		} catch (UnsupportedFileFormatException e) {
			return ResponseEntity.badRequest().body(TransactionProcessingConstants.UNSUPPORTED_FILE_FORMAT);
		} catch (FileProcessingException e) {
			return ResponseEntity.badRequest().body(TransactionProcessingConstants.FILE_PROCESSING_ERROR);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(TransactionProcessingConstants.FILE_PROCESSING_ERROR);
		}

		return ResponseEntity.ok(TransactionProcessingConstants.FILE_UPLOAD_SUCCESSFUL);
	}

	private boolean isValidFileType(String fileName) {
		String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		return Set.of("csv", "xls", "xlsx", "txt").contains(fileExtension);
	}

}
