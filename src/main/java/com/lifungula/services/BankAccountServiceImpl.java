package com.lifungula.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifungula.dtos.CustomerDTO;
import com.lifungula.entities.AccountOperation;
import com.lifungula.entities.BankAccount;
import com.lifungula.entities.CurrentAccount;
import com.lifungula.entities.Customer;
import com.lifungula.entities.SavingAccount;
import com.lifungula.enums.OperationType;
import com.lifungula.exception.BalanceNotSufficientException;
import com.lifungula.exception.BankAccountNotFoundException;
import com.lifungula.exception.CustomerNotFoundException;
import com.lifungula.mappers.BankAccountMapperImpl;
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
	private BankAccountMapperImpl dtoMapper;
	
	@Override
	public Customer saveCustomer(Customer customer) {
		log.info("Saving new customer");
		Customer saveCustomer = customerRepository.save(customer);
		return saveCustomer;
	}

	@Override
	public List<CustomerDTO> ListCustomers() {
		 List<Customer> customers = customerRepository.findAll();
		 List<CustomerDTO> collect = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
		 return collect;
	}

	@Override
	public BankAccount getBankAccount(String accountid) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountid).orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found")); 
		return bankAccount;
	}

	@Override
	public void debit(String accountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
		BankAccount bankAccount = getBankAccount(accountID);
		if(bankAccount.getBalance()<amount) throw new BalanceNotSufficientException("Balance not sufficient");
		
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()-amount);
		bankAccountRepository.save(bankAccount);
		
	}

	@Override
	public void credit(String accountID, double amount, String description) throws BankAccountNotFoundException {
		BankAccount bankAccount = getBankAccount(accountID);
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()+amount);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
		debit(accountIdSource, amount, "transfer to "+accountIdDestination);
		credit(accountIdDestination,amount, "transfer from "+accountIdSource);
		
	}

	@Override
	public CurrentAccount saveCurrentBankAccount(double intialBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException {
		
		Customer customer = customerRepository.findById(customerId).orElse(null);
		CurrentAccount bankAccount = new CurrentAccount();
		if(customer==null) {
			throw new CustomerNotFoundException("customer not found");
		}else {
		bankAccount.setId(UUID.randomUUID().toString());
		bankAccount.setCreatedAt(new Date());
		bankAccount.setBalance(intialBalance);
		bankAccount.setOverDraft(overDraft);
		bankAccount.setCustomer(customer);
		}
	 	return bankAccountRepository.save(bankAccount);
	}

	@Override
	public SavingAccount saveSavingBankAccount(double intialBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException {
		

		Customer customer = customerRepository.findById(customerId).orElse(null);
		SavingAccount bankAccount = new SavingAccount ();
		if(customer==null) {
			throw new CustomerNotFoundException("customer not found");
		}else {
		bankAccount.setId(UUID.randomUUID().toString());
		bankAccount.setCreatedAt(new Date());
		bankAccount.setBalance(intialBalance);
		bankAccount.setInterestRate(interestRate);
		bankAccount.setCustomer(customer);
		System.out.println("créer");
		}
	 	return bankAccountRepository.save(bankAccount);
	 	
	}
	
	@Override
	public List<BankAccount> bankAccountList(){
		return bankAccountRepository.findAll();
	}
	
	@Override
	public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId)
		.orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
		return dtoMapper.fromCustomer(customer);
	}

}
