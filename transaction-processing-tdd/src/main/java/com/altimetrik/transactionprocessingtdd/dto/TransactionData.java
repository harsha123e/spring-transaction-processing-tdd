package com.altimetrik.transactionprocessingtdd.dto;

public class TransactionData {

	private String transactionId;
	private String type;
	private String cardNumber;
	private String expiryDate;
	private String upiId;
	private String cvv;
	private String walletType;
	private double balance;
	private double amount;
	private String remarks;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
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

	@Override
	public String toString() {
		return "TransactionData [transactionId=" + transactionId + ", type=" + type + ", cardNumber=" + cardNumber
				+ ", expiryDate=" + expiryDate + ", upiId=" + upiId + ", cvv=" + cvv + ", walletType=" + walletType
				+ ", balance=" + balance + ", amount=" + amount + ", remarks=" + remarks + "]";
	}

}
