package com.henry.demo.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.henry.demo.domain.Account;
import com.henry.demo.domain.InterestRule;
import com.henry.demo.domain.Transaction;
import com.henry.demo.domain.TransactionTypeEnum;
import com.henry.demo.exception.BalanceNotEnoughException;

public class TransactionServiceImpl implements TransactionService {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static final DecimalFormat df = new DecimalFormat("0.00");
	int runningNumber = 1;
	AccountService accService;
	InterestService interestService;
	List<Transaction> transactionList;

	public TransactionServiceImpl(InterestService interestService) {
		accService = new AccountServiceImpl();
		transactionList = new ArrayList<Transaction>();
		this.interestService = interestService;
	}

	@Override
	public String createNewTransaction(String input) throws Exception {
		String[] transactionInfo = input.split("\\|");

		// check runningNumber
		CharSequence stxnDate = new StringBuffer(transactionInfo[0]);
		Transaction item = transactionList.stream().filter(t -> t.getTxnId().contains(stxnDate)).findAny().orElse(null);
		if (item != null)
			runningNumber++;

		// check account
		String accountId = transactionInfo[1];
		Account acc = accService.getAccount(accountId);

		if (acc == null) {
			if (transactionInfo[2].compareToIgnoreCase("D") != 0) {
				throw new BalanceNotEnoughException("You first need to deposit before withdrawal!");
			} else {

				Date date = sdf.parse(transactionInfo[0]);
				Instant txnDate = date.toInstant();
				double amount = Double.parseDouble(transactionInfo[3]);
				String txnId = String.format("%1$s-%2$s", transactionInfo[0],
						runningNumber > 9 ? runningNumber : "0" + runningNumber);
				Transaction transaction = new Transaction(txnDate, amount, txnId, TransactionTypeEnum.D, accountId,
						amount);
				transactionList.add(transaction);

				Account account = new Account(accountId, amount);
				accService.createAccount(account);

			}
		} else {
			TransactionTypeEnum type = transactionInfo[2].compareToIgnoreCase("D") != 0 ? TransactionTypeEnum.W
					: TransactionTypeEnum.D;

			Date date = sdf.parse(transactionInfo[0]);
			Instant txnDate = date.toInstant();
			double amount = Double.parseDouble(transactionInfo[3]);
			String txnId = String.format("%1$s-%2$s", transactionInfo[0],
					runningNumber > 9 ? runningNumber : "0" + runningNumber);

			switch (type) {
			case D:
				acc.setBalance(acc.getBalance() + amount);
				break;
			case W:
				double balance = acc.getBalance() - amount;
				if (balance < 0) {
					throw new BalanceNotEnoughException("Your balance is not enough for this!");
				} else {
					acc.setBalance(balance);
				}
				break;
			default:
				break;
			}

			Transaction transaction = new Transaction(txnDate, amount, txnId, type, accountId, acc.getBalance());
			transactionList.add(transaction);
		}

		return accountId;
	}

	@Override
	public List<Transaction> getTransactionByAccountId(String accountId) {
		List<Transaction> itemList = transactionList.stream().filter(t -> t.getAccountId().equals(accountId)).toList();
		return itemList;
	}

	@Override
	public List<Transaction> getTransactionByAccountIdAndMonth(String input) throws Exception {

		List<Transaction> filterList = new ArrayList<Transaction>();
		Calendar calendar = Calendar.getInstance();
		String[] accountInfo = input.split("\\|");
		String accountId = accountInfo[0];
		int month = Integer.parseInt(accountInfo[1]);
		int year = calendar.get(Calendar.YEAR);
		int monthMaxDays = 0;

		List<InterestRule> givenMonthRule = interestService.getInterestRuleByMonth(month);
		InterestRule previousRule = interestService.getLatestInterestRuleLessThanGivenMonth(month);
		InterestRule currentRule = givenMonthRule.size() < 1 ? previousRule : givenMonthRule.get(0);

		// System.out.println(givenMonthRule.size());
		// System.out.println(previousRule.getRuleId());

		List<Transaction> itemList = transactionList.stream().filter(t -> t.getAccountId().equals(accountId)).toList();
		for (Transaction txn : itemList) {
			Date date = Date.from(txn.getTxnDate());
			calendar.setTime(date);
			// calendar month start from 0, so need to add 1
			if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month) {
				filterList.add(txn);
				monthMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
		}

		double eodBalance = 0;
		double count = 0;
		double rate = 0;
		double total = 0;
		Instant lastDay = null;
		for (int j = 1; j <= monthMaxDays; j++) {
			count++;
			String sDate = String.format("%1$s%2$s%3$s", year, month < 10 ? "0" + month : month, j < 10 ? "0" + j : j);
			Date date = sdf.parse(sDate);
			Instant txnDate = date.toInstant();

			List<Transaction> currentTxnList = filterList.stream().filter(t -> t.getTxnDate().equals(txnDate)).toList();

			// System.out.println("currentTxnList " + currentTxnList.size());
			if (j == monthMaxDays)
				lastDay = txnDate;

			// more than 1 transaction
			if (currentTxnList.size() > 1) {
				Transaction currentTxn = currentTxnList.stream()
						.sorted((o1, o2) -> o1.getTxnId().compareTo(o2.getTxnId())).collect(Collectors.toList())
						.get(currentTxnList.size() - 1);
				if (currentTxn.getTxnDate().isBefore(currentRule.getTxnDate())) {
					rate = previousRule.getRate() / 100;
					if (eodBalance == 0) {
						eodBalance = currentTxn.getBalance();
					} else if (eodBalance != currentTxn.getBalance()) {
						total += eodBalance * rate * (count - 1);
						eodBalance = currentTxn.getBalance();
						count = 1;
						// System.out.println("1 Calcuate Multiple before Total " + total);
					}
				} else {
					rate = currentRule.getRate() / 100;
					if (eodBalance == 0) {
						eodBalance = currentTxn.getBalance();
					} else if (eodBalance != currentTxn.getBalance()) {
						total += eodBalance * rate * (count - 1);
						eodBalance = currentTxn.getBalance();
						count = 1;
						// System.out.println("1 Calcuate Multiple After Total " + total);
					}
				}
			}
			// only one transaction
			else if (currentTxnList.size() == 1) {
				Transaction currentTxn = currentTxnList.get(0);
				if (currentTxn.getTxnDate().isBefore(currentRule.getTxnDate())) {
					rate = previousRule.getRate() / 100;
					if (eodBalance == 0) {
						eodBalance = currentTxn.getBalance();
					} else if (eodBalance != currentTxn.getBalance()) {
						total += eodBalance * rate * (count - 1);
						eodBalance = currentTxn.getBalance();
						count = 1;
						// System.out.println("2 Calcuate Single Before Total " + total);
					}
				} else {
					rate = currentRule.getRate() / 100;
					if (eodBalance == 0) {
						eodBalance = currentTxn.getBalance();
					} else if (eodBalance != currentTxn.getBalance()) {
						total += eodBalance * rate * (count - 1);
						eodBalance = currentTxn.getBalance();
						count = 1;
						// System.out.println("2 Calcuate Single After Total " + total);
					}
				}
			} else {
				if (txnDate.isBefore(currentRule.getTxnDate())) {
					rate = previousRule.getRate() / 100;
					total += eodBalance * rate * (count);
					count = 0;
					// System.out.println("3 Calcuate Before Total " + total);
				} else {
					rate = currentRule.getRate() / 100;
					total += eodBalance * rate * (count);
					count = 0;
					// System.out.println("3 Calcuate After Total " + total);
				}
			}
		}
		// System.out.println("Total " + total);
		total = total / 365;
		// System.out.println("Total interest: " + df.format(total));
		Transaction interestTxn = new Transaction(lastDay, total, "", TransactionTypeEnum.I, accountId,
				eodBalance + total);
		filterList.add(interestTxn);

		return filterList;
	}

}
