package com.altimetrik.transactionprocessingtdd.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TransactionController {

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is empty");
		}

		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null && !isValidFileType(originalFilename)) {
			return ResponseEntity.badRequest().body("Invalid file type");
		}

		return ResponseEntity.ok("File uploaded successfully");
	}

	private boolean isValidFileType(String fileName) {
		String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		return Set.of("csv", "xls", "xlsx", "txt").contains(fileExtension);
	}

}
