package com.henry.demo.service;

import com.henry.demo.domain.Account;

public interface AccountService {

	public void createAccount(Account accountInfo);
	
	public Account getAccount(String accountId);
}
