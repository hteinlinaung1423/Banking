package com.henry.demo.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.henry.demo.domain.InterestRule;

public class InterestServiceImpl implements InterestService {
	List<InterestRule> ruleList;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public InterestServiceImpl() {
		ruleList = new ArrayList<InterestRule>();
	}

	@Override
	public void createNewInterest(String input) throws Exception {
		String[] interestInfo = input.split("\\|");
		String ruleId = interestInfo[1];
		Date date = sdf.parse(interestInfo[0]);
		Instant txnDate = date.toInstant();
		double amount = Double.parseDouble(interestInfo[2]);

		InterestRule rule = ruleList.stream()
				.filter(r -> txnDate.equals(r.getTxnDate()) || ruleId.equals(r.getRuleId())).findAny().orElse(null);
		if (rule != null) {
			rule.setRate(amount);
			rule.setRuleId(ruleId);
			rule.setTxnDate(txnDate);
		} else {
			InterestRule interest = new InterestRule(txnDate, amount, ruleId);
			ruleList.add(interest);
		}

	}

	@Override
	public List<InterestRule> getAll() {
		return ruleList.stream().sorted((o1, o2) -> o1.getTxnDate().compareTo(o2.getTxnDate()))
				.collect(Collectors.toList());
	}

	@Override
	public List<InterestRule> getInterestRuleByMonth(int month) {
		List<InterestRule> filterList = new ArrayList<InterestRule>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		//System.out.println("year1" + year);
		//System.out.println("month1" + month);
		for (InterestRule item : ruleList) {
			Date date = Date.from(item.getTxnDate());
			calendar.setTime(date);
			//System.out.println("year2" + calendar.get(Calendar.YEAR));
			//System.out.println("month2" + calendar.get(Calendar.MONTH));
			if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month) // calendar month
																									// start from 0, so
																									// need to add 1
				filterList.add(item);
		}
		//System.out.println(filterList.size());
		return filterList.stream().sorted((o1, o2) -> o1.getTxnDate().compareTo(o2.getTxnDate()))
				.collect(Collectors.toList());
	}

	@Override
	public InterestRule getLatestInterestRuleLessThanGivenMonth(int month) {
		List<InterestRule> filterList = new ArrayList<InterestRule>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		for (InterestRule item : this.ruleList) {
			Date date = Date.from(item.getTxnDate());
			calendar.setTime(date);
			if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 < month) // calendar month
																									// start from 0, so
																									// need to add 1
				filterList.add(item);
		}

		if (filterList.size() > 0)
			return filterList.stream().sorted((o1, o2) -> o1.getTxnDate().compareTo(o2.getTxnDate()))
					.collect(Collectors.toList()).get(filterList.size() - 1); // get the latest one;
		else
			return null;
	}

}
