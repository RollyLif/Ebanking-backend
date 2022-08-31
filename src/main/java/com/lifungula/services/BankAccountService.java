package com.lifungula.services;

import java.util.List;

import com.lifungula.entities.BankAccount;
import com.lifungula.entities.CurrentAccount;
import com.lifungula.entities.Customer;
import com.lifungula.entities.SavingAccount;
import com.lifungula.exception.BalanceNotSufficientException;
import com.lifungula.exception.BankAccountNotFoundException;
import com.lifungula.exception.CustomerNotFoundException;

public interface BankAccountService {
	
	Customer saveCustomer(Customer customer);
	CurrentAccount saveCurrentBankAccount(double intialBalance,double overDraft, Long customerId) throws CustomerNotFoundException;
	SavingAccount saveSavingBankAccount(double intialBalance,double interestRate, Long customerId) throws CustomerNotFoundException;
	List<Customer> ListCustomers();
	BankAccount getBankAccount(String accountid) throws BankAccountNotFoundException;
	void debit(String accountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
	void credit(String accountID, double amount, String description) throws BankAccountNotFoundException;
	void transfer(String accountIdSource, String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	
}
