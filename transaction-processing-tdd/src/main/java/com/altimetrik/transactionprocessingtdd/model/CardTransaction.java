package com.altimetrik.transactionprocessingtdd.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CardTransaction {

	@Id
	private String transactionId;
	private String cardNumber;
	private String expiryDate;
	private String cvv;
	private double amount;
	private String remarks;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
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

	@Override
	public String toString() {
		return "CardTransaction [transactionId=" + transactionId + ", cardNumber=" + cardNumber + ", expiryDate="
				+ expiryDate + ", cvv=" + cvv + ", amount=" + amount + ", remarks=" + remarks + "]";
	}

}
