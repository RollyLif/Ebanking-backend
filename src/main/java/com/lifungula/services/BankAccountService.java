package com.lifungula.services;

import java.util.List;

import com.lifungula.dtos.BankAccountDTO;
import com.lifungula.dtos.CurrentBankAccountDTO;
import com.lifungula.dtos.CustomerDTO;
import com.lifungula.dtos.SavingBankAccountDTO;
import com.lifungula.exception.BalanceNotSufficientException;
import com.lifungula.exception.BankAccountNotFoundException;
import com.lifungula.exception.CustomerNotFoundException;

public interface BankAccountService {
	
	CustomerDTO saveCustomer(CustomerDTO customerDTO);
	CurrentBankAccountDTO saveCurrentBankAccount(double intialBalance,double overDraft, Long customerId) throws CustomerNotFoundException;
	SavingBankAccountDTO saveSavingBankAccount(double intialBalance,double interestRate, Long customerId) throws CustomerNotFoundException;
	List<CustomerDTO> ListCustomers();
	BankAccountDTO getBankAccount(String accountid) throws BankAccountNotFoundException;
	void debit(String accountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
	void credit(String accountID, double amount, String description) throws BankAccountNotFoundException;
	void transfer(String accountIdSource, String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	List<BankAccountDTO> bankAccountList();
	CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
	CustomerDTO updateCustomer(CustomerDTO customerDTO);
	void deleteCustomer(Long customerId);
	
}
