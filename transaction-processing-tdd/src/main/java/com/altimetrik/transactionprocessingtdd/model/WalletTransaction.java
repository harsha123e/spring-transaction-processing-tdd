package com.altimetrik.transactionprocessingtdd.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class WalletTransaction {

	@Id
	private String transactionId;

	@NotBlank(message = "Wallet type is required")
	private String walletType;

	@NotBlank(message = "UPI Id is required")
	private String upiId;

	@DecimalMin(value = "0.01", message = "Amount must be greater than or equal to 0.01")
	private double balance;

	@DecimalMin(value = "0.01", message = "Amount must be greater than or equal to 0.01")
	private double amount;

	@Size(max = 100, message = "Remarks must be at most 100 characters")
	private String remarks;

	public WalletTransaction() {
		super();
	}

	public WalletTransaction(String transactionId, String walletType, String upiId, double balance, double amount,
			String remarks) {
		super();
		this.transactionId = transactionId;
		this.walletType = walletType;
		this.upiId = upiId;
		this.balance = balance;
		this.amount = amount;
		this.remarks = remarks;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
		return "WalletTransaction [transactionId=" + transactionId + ", walletType=" + walletType + ", upiId=" + upiId
				+ ", balance=" + balance + ", amount=" + amount + ", remarks=" + remarks + "]";
	}

}
