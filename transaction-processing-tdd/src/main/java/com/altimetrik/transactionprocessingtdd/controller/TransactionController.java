package com.altimetrik.transactionprocessingtdd.controller;

import static com.altimetrik.transactionprocessingtdd.utils.TransactionProcessingConstants.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altimetrik.transactionprocessingtdd.service.TransactionService;
import com.altimetrik.transactionprocessingtdd.utils.FileUtil;

@RestController
public class TransactionController {

	private TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(FILE_IS_EMPTY);
		}

		String filename = file.getOriginalFilename();
		if (filename == null || filename.isBlank()) {
			return ResponseEntity.badRequest().body(INVALID_FILE_NAME);
		}

		String fileExtension = FileUtil.getFileExtension(filename);
		if (!FileUtil.isValidExtension(fileExtension)) {
			return ResponseEntity.badRequest().body(INVALID_FILE_EXTENSION + fileExtension);
		}

		if (!FileUtil.isFileTypeMatching(filename, file.getContentType())) {
			return ResponseEntity.badRequest().body(INVALID_FILE_TYPE);
		}

		try {
			transactionService.processAndSaveTransactions(file, fileExtension);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(FILE_UPLOAD_SUCCESSFUL);
	}

}
