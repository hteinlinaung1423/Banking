package com.henry.demo.domain;

import java.util.List;

public class Account {
	private String accountId;
	private List<Transaction> transactionList;
	private double balance;

	public Account(String accountId, double balance) {
		super();
		this.accountId = accountId;
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

}
