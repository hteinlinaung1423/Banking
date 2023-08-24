package com.henry.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.henry.demo.domain.Transaction;
import com.henry.demo.exception.BalanceNotEnoughException;
import com.henry.demo.service.InterestService;
import com.henry.demo.service.InterestServiceImpl;
import com.henry.demo.service.TransactionService;
import com.henry.demo.service.TransactionServiceImpl;

public class AppTest {

	InterestService interestService = new InterestServiceImpl();
	TransactionService transactionService = new TransactionServiceImpl(interestService);

	@Test
	public void Test() {
		assertEquals(2, 1 + 1);
	}

	@Test
	public void AddNewTransactionTest() {
		String input = "20230505|AC001|W|100.00";
		String accId;
		try {
			accId = transactionService.createNewTransaction(input);
			assertEquals(accId, "AC001");
		}catch (BalanceNotEnoughException e) {
			String expectedMessage = "You first need to deposit before withdrawal!";
		    String actualMessage = e.getMessage();
		    assertTrue(actualMessage.contains(expectedMessage));
		} catch (Exception e) {
			//System.out.println(e.getMessage()+"\r\n");
		}
	}

    @Test
	public void BalanceNotEnoughTransactionTest() {
		String input = "20230505|AC001|D|100.00";
        String input2 = "20230505|AC001|W|150.00";
		String accId;
		try {
			transactionService.createNewTransaction(input);
            accId = transactionService.createNewTransaction(input2);
			assertEquals(accId, "AC001");
		}catch (BalanceNotEnoughException e) {
			String expectedMessage = "Your balance is not enough for this!";
		    String actualMessage = e.getMessage();
		    assertTrue(actualMessage.contains(expectedMessage));
		} catch (Exception e) {
			//System.out.println(e.getMessage()+"\r\n");
		}
	}

    @Test
	public void AddNewInterestRuleTest() {
		String input = "20230101|RULE01|1.95";
        String input2 = "20230520|RULE02|1.90";
		try {
			interestService.createNewInterest(input);
            interestService.createNewInterest(input2);
            assertEquals(2,interestService.getAll().size());
		}catch (Exception e) {
			//System.out.println(e.getMessage()+"\r\n");
		}
	}

    @SuppressWarnings("deprecation")
	@Test
	public void InterestRateCalculationTest() {

        String input1 = "20230505|AC001|D|100.00";
        String input2 = "20230601|AC001|D|150.00";
        String input3 = "20230626|AC001|W|20.00";
        String input4 = "20230626|AC001|W|100.00";
		String input5 = "20230520|RULE02|1.90";
        String input6 = "20230615|RULE03|2.20";
        String input7= "AC001|06";
		try {
            transactionService.createNewTransaction(input1);
            transactionService.createNewTransaction(input2);
            transactionService.createNewTransaction(input3);
            transactionService.createNewTransaction(input4);
			interestService.createNewInterest(input5);
            interestService.createNewInterest(input6);

            List<Transaction> transactionList=transactionService.getTransactionByAccountIdAndMonth(input7);

            assertEquals(130.38712328767124,transactionList.get(transactionList.size()-1).getBalance(),0.0f);
            
		}catch (Exception e) {
			//System.out.println(e.getMessage()+"\r\n");
		}
	}

}
