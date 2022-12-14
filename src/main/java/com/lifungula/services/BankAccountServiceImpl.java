package com.lifungula.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifungula.dtos.AccountHistoryDTO;
import com.lifungula.dtos.AccountOperationDTO;
import com.lifungula.dtos.BankAccountDTO;
import com.lifungula.dtos.CurrentBankAccountDTO;
import com.lifungula.dtos.CustomerDTO;
import com.lifungula.dtos.DebitDTO;
import com.lifungula.dtos.SavingBankAccountDTO;
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
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
		log.info("Saving new customer");
		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
		Customer saveCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(saveCustomer);
	}

	@Override
	public List<CustomerDTO> ListCustomers() {
		 List<Customer> customers = customerRepository.findAll();
		 List<CustomerDTO> collect = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
		 return collect;
	}

	@Override
	public BankAccountDTO getBankAccount(String accountid) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountid).get();/*.orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"))*/; 
		if(bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return dtoMapper.fromSavingBankAccount(savingAccount);
		}else {
			CurrentAccount currentAccount = (CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentBankAccount(currentAccount);
		}
	}

	@Override
	public void debit(DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
	    System.out.println(debitDTO.getAccountId());
	    BankAccount bankAccount = bankAccountRepository.findById(debitDTO.getAccountId()).orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));
		if(bankAccount.getBalance()< debitDTO.getAmount()) throw new BalanceNotSufficientException("Balance not sufficient");
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(debitDTO.getAmount());
		accountOperation.setDescription(debitDTO.getDescription());
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()-debitDTO.getAmount());
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void credit(String accountID, double amount, String description) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountID).orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));
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
		DebitDTO debitDTO = new DebitDTO();
		debitDTO.setAccountId(accountIdSource);
		debitDTO.setAmount(amount);
		debitDTO.setDescription("transfer to "+accountIdDestination);
	    debit(debitDTO);
		credit(accountIdDestination,amount, "transfer from "+accountIdSource);
		System.out.println("transfert effectu?? avec succ??s");
	}

	@Override
	public CurrentBankAccountDTO saveCurrentBankAccount(double intialBalance, double overDraft, Long customerId)
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
	 	return dtoMapper.fromCurrentBankAccount(bankAccountRepository.save(bankAccount));
	}

	@Override
	public SavingBankAccountDTO saveSavingBankAccount(double intialBalance, double interestRate, Long customerId)
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
		}
	 	return dtoMapper.fromSavingBankAccount(bankAccountRepository.save(bankAccount));
	}
	
	@Override
	public List<BankAccountDTO> bankAccountList(){
		List<BankAccount> bankAccounts = bankAccountRepository.findAll();
		List<BankAccountDTO> bankAccountDTOs = bankAccounts.stream().map(bankaccount ->{
			if(bankaccount instanceof SavingAccount) {
				SavingAccount savingAccount = (SavingAccount) bankaccount;
				return dtoMapper.fromSavingBankAccount(savingAccount);
			}else {
				CurrentAccount currentAccount = (CurrentAccount) bankaccount;
				return dtoMapper.fromCurrentBankAccount(currentAccount);
			}
		}).collect(Collectors.toList());
		
		return bankAccountDTOs;
	}
	
	@Override
	public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId)
		.orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
		return dtoMapper.fromCustomer(customer);
	}
	
	@Override
	public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
		log.info("Saving new customer");
		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
		Customer saveCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(saveCustomer);
	}
	
	@Override
	public void deleteCustomer(Long customerId) {
		customerRepository.deleteById(customerId);
	}
	
	@Override
	public List<AccountOperationDTO> accountHistory(String accountId){
		List<AccountOperation> bai=accountOperationRepository.findByBankAccountId(accountId);
		return bai.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
	}

	@Override
	public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
		if(bankAccount==null) throw new BankAccountNotFoundException("Account not Found");
		Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId,PageRequest.of(page, size));
		AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
		List<AccountOperationDTO> collect = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
		accountHistoryDTO.setAccountOperationDTOs(collect);
		accountHistoryDTO.setAccountId(bankAccount.getId());
		accountHistoryDTO.setBalance(bankAccount.getBalance());
		accountHistoryDTO.setCurrentPage(page);
		accountHistoryDTO.setPageSize(size);
		accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
		return accountHistoryDTO;
	}

	@Override
	public List<CustomerDTO> searchCustomers(String keyword) {
		List<Customer> customer = customerRepository.searchCustomer(keyword);
		List<CustomerDTO> collect = customer.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
		return collect;
	}

}
