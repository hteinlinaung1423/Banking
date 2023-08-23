package com.henry.demo.domain;

import java.time.Instant;

public class Transaction {
	private Instant txnDate;
	private double amount;
	private String txnId;
	private TransactionTypeEnum transactionType;
	private String accountId;
	private double balance;

	public Transaction() {
	}

	public Transaction(Instant txnDate, double amount, String txnId, TransactionTypeEnum transactionType,
			String accountId,double balance) {
		super();
		this.txnDate = txnDate;
		this.amount = amount;
		this.txnId = txnId;
		this.transactionType = transactionType;
		this.accountId = accountId;
		this.balance=balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Instant getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Instant txnDate) {
		this.txnDate = txnDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}

}
