package com.henry.demo.service;

import java.util.List;

import com.henry.demo.domain.InterestRule;

public interface InterestService {
    public void createNewInterest(String interestInfo) throws Exception;

    public List<InterestRule> getAll();

    public List<InterestRule> getInterestRuleByMonth(int month);

    public InterestRule getLatestInterestRuleLessThanGivenMonth(int month);
}
