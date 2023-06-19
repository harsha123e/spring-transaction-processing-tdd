package com.altimetrik.transactionprocessingtdd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TransactionController {

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType != null && !isValidFileType(contentType)) {
			return ResponseEntity.badRequest().body("Invalid file type");
		}
		return ResponseEntity.ok("File uploaded successfully");
	}

	private boolean isValidFileType(String contentType) {
		return contentType.equals("application/vnd.ms-excel")
				|| contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				|| contentType.equals("text/csv") || contentType.startsWith("text/");
	}
}
