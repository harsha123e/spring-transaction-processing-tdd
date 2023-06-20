package com.altimetrik.transactionprocessingtdd.dto;

import java.time.LocalDate;

public class TransactionDto {
	private String transactionId;
	private String transactionType;
	private String cardNumber;
	private LocalDate expiryDate;
	private int cvv;
	private double amount;
	private String remarks;
	private String walletType;
	private String upiId;
	private double balance;

	public TransactionDto() {
	}

	public TransactionDto(String transactionId, String transactionType, double amount, String remarks) {
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.remarks = remarks;
	}

	public TransactionDto(String transactionId, String transactionType, String cardNumber, LocalDate expiryDate,
			int cvv, double amount, String remarks) {
		this(transactionId, transactionType, amount, remarks);
		this.cardNumber = cardNumber;
		this.expiryDate = expiryDate;
		this.cvv = cvv;

	}

	public TransactionDto(String transactionId, String transactionType, String walletType, String upiId, double balance,
			double amount, String remarks) {
		this(transactionId, transactionType, amount, remarks);
		this.walletType = walletType;
		this.upiId = upiId;
		this.balance = balance;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
		this.cvv = cvv;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "TransactionDto [transactionId=" + transactionId + ", transactionType=" + transactionType
				+ ", cardNumber=" + cardNumber + ", expiryDate=" + expiryDate + ", cvv=" + cvv + ", amount=" + amount
				+ ", remarks=" + remarks + ", walletType=" + walletType + ", upiId=" + upiId + ", balance=" + balance
				+ "]";
	}

}
