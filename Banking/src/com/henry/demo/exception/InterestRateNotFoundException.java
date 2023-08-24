package com.henry.demo.exception;

public class InterestRateNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterestRateNotFoundException(String statement) {
		super(statement);
	}
}