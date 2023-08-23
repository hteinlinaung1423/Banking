package com.henry.demo.util;

public class ValidationUtil {

	public static String validateTransactionInput(String input) {
		String message = "";

		String[] transactionData = input.split("\\|");

		if (transactionData.length != 4) {
			message += "Melformed input!\r\n";
		} else {
			// date
			if (transactionData[0].length() != 8) {
				message += "Melformed date input!\r\n";
			} else {
				try {
					int year = Integer.parseInt(transactionData[0].substring(0, 4));
					int month = Integer.parseInt(transactionData[0].substring(4, 6));
					int day = Integer.parseInt(transactionData[0].substring(6, 8));
					if (month > 12 || month <= 0 || day > 31 || day <= 0 || year < 2000) {
						message += "Melformed date input!\r\n";
					}
				} catch (NumberFormatException ex) {
					message += "Melformed date input!\r\n";
				}
			}

			// transaction type
			if (transactionData[2].compareToIgnoreCase("D") != 0 && transactionData[2].compareToIgnoreCase("W") != 0) {
				// message += "";
				message = "Melformed transaction type input!\r\n";
			}

			// amount
			try {
				// double amount = Double.parseDouble(transactionData[3]);
				String[] strArr = transactionData[3].split("\\.");
				if (strArr.length > 1 && strArr[1].length() > 2) {
					message += "Decimals amount are allowed up to 2 decimal places!\r\n";
				}
			} catch (NumberFormatException ex) {
				message += "Melformed amount input!\r\n";
			}
		}

		return message;
	}

	public static String validateInterestRateInput(String input) {
		String message = "";

		String[] transactionData = input.split("\\|");

		if (transactionData.length != 3) {
			message += "Melformed input!\r\n";
		} else {
			// date
			if (transactionData[0].length() != 8) {
				message += "Melformed date input!\r\n";
			} else {
				try {
					int year = Integer.parseInt(transactionData[0].substring(0, 4));
					int month = Integer.parseInt(transactionData[0].substring(4, 6));
					int day = Integer.parseInt(transactionData[0].substring(6, 8));
					if (month > 12 || month <= 0 || day > 31 || day <= 0 || year < 2000) {
						message += "Melformed date input!\r\n";
					}
				} catch (NumberFormatException ex) {
					message += "Melformed date input!\r\n";
				}
			}

			// amount
			try {
				double amount = Double.parseDouble(transactionData[2]);
				String[] strArr = transactionData[2].split("\\.");
				if (strArr.length > 1 && strArr[1].length() > 2) {
					message += "Decimals amount are allowed up to 2 decimal places!\r\n";
				} else if (amount <= 0 || amount >= 100) {
					message += "Melformed interest rate amount!\r\n";
				}
			} catch (NumberFormatException ex) {
				message += "Melformed amount input!\r\n";
			}
		}

		return message;
	}

	public static String validatePrintStatementInput(String input) {
		String message = "";

		String[] transactionData = input.split("\\|");

		if (transactionData.length != 2) {
			message += "Melformed input!\r\n";
		} else {
			// month
			try {
				int month = Integer.parseInt(transactionData[1]);
				if (month > 12 || month <= 0) {
					message += "Melformed month input!\r\n";
				}
			} catch (NumberFormatException ex) {
				message += "Melformed month input!\r\n";
			}
		}

		return message;
	}
}
