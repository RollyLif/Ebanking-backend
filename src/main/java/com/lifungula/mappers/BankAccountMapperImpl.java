package com.lifungula.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.lifungula.dtos.AccountOperationDTO;
import com.lifungula.dtos.CurrentBankAccountDTO;
import com.lifungula.dtos.CustomerDTO;
import com.lifungula.dtos.SavingBankAccountDTO;
import com.lifungula.entities.AccountOperation;
import com.lifungula.entities.CurrentAccount;
import com.lifungula.entities.Customer;
import com.lifungula.entities.SavingAccount;

@Service
public class BankAccountMapperImpl {

	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();	
		BeanUtils.copyProperties(customer, customerDTO);
//		customerDTO.setId(customer.getId());
//		customerDTO.setEmail(customer.getEmail());
//		customerDTO.setName(customer.getName());
		return customerDTO;
	}
	
	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}
	
	public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
		SavingBankAccountDTO savDTO=new SavingBankAccountDTO();
		BeanUtils.copyProperties(savingAccount, savDTO);
		savDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
		savDTO.setType(savingAccount.getClass().getSimpleName());
		return savDTO;
	}
	
	public SavingAccount fromSavingAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
		SavingAccount savAc= new SavingAccount();
		BeanUtils.copyProperties(savingBankAccountDTO, savAc);
		savAc.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
		return savAc;
	}
	
	public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
		CurrentBankAccountDTO savDTO=new CurrentBankAccountDTO();
		BeanUtils.copyProperties(currentAccount, savDTO);
		savDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
		savDTO.setType(currentAccount.getClass().getSimpleName());
		return savDTO;
	}
	
	public CurrentAccount fromCurrentAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
		CurrentAccount savAc= new CurrentAccount();
		BeanUtils.copyProperties(currentBankAccountDTO, savAc);
		savAc.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
		return savAc;
	}
	
	public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
		AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
		BeanUtils.copyProperties(accountOperation, accountOperationDTO);
		return accountOperationDTO;
	}
}
