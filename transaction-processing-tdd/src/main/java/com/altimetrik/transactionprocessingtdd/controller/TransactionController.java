package com.altimetrik.transactionprocessingtdd.controller;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.FILE_UPLOAD_SUCCESSFUL;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.exception.EmptyFileException;
import com.altimetrik.transactionprocessingtdd.exception.FileProcessingException;
import com.altimetrik.transactionprocessingtdd.exception.UnsupportedFileFormatException;
import com.altimetrik.transactionprocessingtdd.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

		try {
			transactionService.processAndSaveTransactions(file);
		} catch (EmptyFileException | UnsupportedFileFormatException | FileProcessingException | IOException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok(FILE_UPLOAD_SUCCESSFUL);
	}

}
