package com.henry.demo.service;

import java.util.List;
import com.henry.demo.domain.Transaction;

public interface TransactionService {

	public String createNewTransaction(String transactionInfo) throws Exception;
	
	public List<Transaction> getTransactionByAccountId(String accountId);

	public List<Transaction> getTransactionByAccountIdAndMonth(String input) throws Exception;
}
