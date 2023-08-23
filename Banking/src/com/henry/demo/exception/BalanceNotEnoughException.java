package com.henry.demo.exception;

public class BalanceNotEnoughException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceNotEnoughException(String statement) {
		super(statement);
	}
}