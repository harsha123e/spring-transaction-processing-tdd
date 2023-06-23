package com.altimetrik.transactionprocessingtdd.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class CardTransaction {

	@Id
	private String transactionId;

	@NotBlank(message = "Card number is required")
	private String cardNumber;

	private LocalDate expiryDate;

	@Min(value = 100, message = "CVV must be at least 100")
	private int cvv;

	@DecimalMin(value = "0.01", message = "Amount must be greater than or equal to 0.01")
	private double amount;

	@Size(max = 100, message = "Remarks must be at most 100 characters")
	private String remarks;

	public CardTransaction() {
	}

	public CardTransaction(String transactionId, String cardNumber, LocalDate expiryDate, int cvv, double amount,
			String remarks) {
		super();
		this.transactionId = transactionId;
		this.cardNumber = cardNumber;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
		this.amount = amount;
		this.remarks = remarks;
	}

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

	@Override
	public String toString() {
		return "CardTransaction [transactionId=" + transactionId + ", cardNumber=" + cardNumber + ", expiryDate="
				+ expiryDate + ", cvv=" + cvv + ", amount=" + amount + ", remarks=" + remarks + "]";
	}

}
