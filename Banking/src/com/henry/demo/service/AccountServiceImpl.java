package com.henry.demo.service;

import java.util.HashSet;
import java.util.Set;

import com.henry.demo.domain.Account;

public class AccountServiceImpl implements AccountService {

	public AccountServiceImpl() {
	}

	Set<Account> accountList = new HashSet<Account>();

	@Override
	public void createAccount(Account account) {
		// TODO Auto-generated method stub
		accountList.add(account);
	}

	@Override
	public Account getAccount(String accountId) {
		// TODO Auto-generated method stub
		Account acc = accountList.stream().filter(account -> accountId.equals(account.getAccountId())).findAny()
				.orElse(null);
		return acc;
	}

}
