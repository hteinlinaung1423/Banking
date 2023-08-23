package com.henry.demo;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.henry.demo.domain.InterestRule;
import com.henry.demo.domain.Transaction;
import com.henry.demo.exception.BalanceNotEnoughException;
import com.henry.demo.service.InterestService;
import com.henry.demo.service.InterestServiceImpl;
import com.henry.demo.service.TransactionService;
import com.henry.demo.service.TransactionServiceImpl;
import com.henry.demo.util.ValidationUtil;

public class Main {

	private static final String PATTERN_FORMAT = "yyyyMMdd";
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		char action = ' ';
		InterestService interestService = new InterestServiceImpl();
		TransactionService transactionService = new TransactionServiceImpl(interestService);
		do {
			System.out.println("Welcome to AwesomeGIC Bank! What would you like to do?\r\n"
					+ "[I]nput transactions \r\n" + "[D]efine interest rules\r\n" + "[P]rint statement\r\n" + "[Q]uit");

			String input = reader.nextLine(); // Read user input
			if (input.length() > 0)
				action = input.charAt(0);

			PerformAction(action, reader, transactionService, interestService);

		} while (action != 'q' && action != 'Q');

		System.out.println("Thank you for banking with AwesomeGIC Bank.\r\n" + "Have a nice day!");
		reader.close();
	}

	public static void PerformAction(char action, Scanner reader, TransactionService transactionService,
			InterestService interestService) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.systemDefault());
		String input = "";

		switch (action) {
		case 'I':
		case 'i':
			do {
				System.out.println("Please enter transaction details in <Date>|<Account>|<Type>|<Amount> format \r\n"
						+ "(or enter blank to go back to main menu):");

				input = reader.nextLine(); // Read user input

				if (input.length() > 0) {
					String message = ValidationUtil.validateTransactionInput(input);

					if (message.length() < 1) {
						try {
							String accId = transactionService.createNewTransaction(input);
							System.out.println("Account: " + accId);
							System.out.println("Date    | Txn Id    |Type| Amount |");
							for (Transaction item : transactionService.getTransactionByAccountId(accId)) {
								System.out.println(
										String.format("%1$s|%2$s| %3$s  |%4$s   |", formatter.format(item.getTxnDate()),
												item.getTxnId(), item.getTransactionType(), item.getAmount()));
							}
							input = "";
						} catch (BalanceNotEnoughException e) {

							System.out.println(e.getMessage());
						} catch (Exception e) {

							System.out.println(e.getMessage());
						}
					} else {
						System.out.println(message);
					}
				}
			} while (input.length() > 0);

			break;
		case 'D':
		case 'd':
			do {
				System.out.println("Please enter interest rules details in <Date>|<RuleId>|<Rate in %> format \r\n"
						+ "(or enter blank to go back to main menu):");

				input = reader.nextLine(); // Read user input
				if (input.length() > 0) {
					String message = ValidationUtil.validateInterestRateInput(input);
					if (message.length() < 1) {
						try {
							interestService.createNewInterest(input);

							System.out.println("Interest rules:");
							System.out.println("Date     | RuleId | Rate (%) |");
							for (InterestRule item : interestService.getAll()) {
								System.out.println(String.format("%1$s | %2$s |   %3$s    |",
										formatter.format(item.getTxnDate()), item.getRuleId(), item.getRate()));
							}
							input = "";
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					} else {
						System.out.println(message);
					}
				}

			} while (input.length() > 0);
			break;
		case 'P':
		case 'p':
			do {
				System.out.println("Please enter account and month to generate the statement <Account>|<Month>\r\n"
						+ "(or enter blank to go back to main menu):");

				input = reader.nextLine(); // Read user input

				if (input.length() > 0) {
					String message = ValidationUtil.validatePrintStatementInput(input);
					if (message.length() < 1) {
						try {
							String[] accountInfo = input.split("\\|");
							System.out.println("Account: " + accountInfo[0]);
							System.out.println("Date    | Txn Id    |Type| Amount | Balance |");
							for (Transaction item : transactionService.getTransactionByAccountIdAndMonth(input)) {
								System.out.println(String.format("%1$s|%2$s| %3$s  |%4$s   |%5$s   |",
										formatter.format(item.getTxnDate()), item.getTxnId().length()>1?item.getTxnId():"           ", item.getTransactionType(),
										df.format(item.getAmount()), df.format(item.getBalance())));
							}
							input = "";
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}

					} else {
						System.out.println(message);
					}
				}

			} while (input.length() > 0);
			break;
		default:
			break;
		}
	}
}

// Character.compare(control, 'q') != 0