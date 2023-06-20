package com.altimetrik.transactionprocessingtdd.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WalletTransaction {

	@Id
	private String transactionId;
	private String walletType;
	private String upiId;
	private double balance;
	private double amount;
	private String remarks;

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
