package com.henry.demo.domain;

import java.time.Instant;

public class InterestRule {
	private Instant txnDate;
	private double rate;
	private String ruleId;

	public InterestRule() {
	}

	public InterestRule(Instant txnDate, double rate, String ruleId) {
		super();
		this.txnDate = txnDate;
		this.rate = rate;
		this.ruleId = ruleId;
	}

	public Instant getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Instant txnDate) {
		this.txnDate = txnDate;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

}
