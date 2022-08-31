package com.lifungula.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifungula.entities.BankAccount;
import com.lifungula.entities.CurrentAccount;
import com.lifungula.entities.Customer;
import com.lifungula.entities.SavingAccount;
import com.lifungula.exception.CustomerNotFoundException;
import com.lifungula.repositories.AccountOperationRepository;
import com.lifungula.repositories.BankAccountRepository;
import com.lifungula.repositories.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

public class BankAccountServiceImpl implements BankAccountService{
	private CustomerRepository customerRepository;
	private BankAccountRepository bankAccountRepository;
	private AccountOperationRepository accountOperationRepository;
	
	
	@Override
	public Customer saveCustomer(Customer customer) {
		log.info("Saving new customer");
		Customer saveCustomer = customerRepository.save(customer);
		return saveCustomer;
	}

	@Override
	public List<Customer> ListCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankAccount getBankAccount(String accountid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void debit(String accountID, double amount, String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void credit(String accountID, double amount, String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transfer(String accountIdSource, String accountIdDestination, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CurrentAccount saveCurrentBankAccount(double intialBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException {
		
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if(customer==null)
			throw new CustomerNotFoundException("customer not found");
		CurrentAccount bankAccount = new CurrentAccount();
		bankAccount.setId(UUID.randomUUID().toString());
		bankAccount.setCreatedAt(new Date());
		bankAccount.setBalance(intialBalance);
		bankAccount.setOverDraft(overDraft);
		bankAccount.setCustomer(customer);
		
	 	return bankAccountRepository.save(bankAccount);
	}

	@Override
	public SavingAccount saveSavingBankAccount(double intialBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException {
		

		Customer customer = customerRepository.findById(customerId).orElse(null);
		if(customer==null)
			throw new CustomerNotFoundException("customer not found");
		SavingAccount bankAccount = new SavingAccount ();
		bankAccount.setId(UUID.randomUUID().toString());
		bankAccount.setCreatedAt(new Date());
		bankAccount.setBalance(intialBalance);
		bankAccount.setInterestRate(interestRate);
		bankAccount.setCustomer(customer);
		
	 	return bankAccountRepository.save(bankAccount);
	}

}
