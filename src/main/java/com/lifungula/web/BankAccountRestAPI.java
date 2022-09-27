package com.lifungula.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.lifungula.dtos.AccountOperationDTO;
import com.lifungula.dtos.BankAccountDTO;
import com.lifungula.exception.BankAccountNotFoundException;
import com.lifungula.services.BankAccountService;

import lombok.AllArgsConstructor;

@RestController
public class BankAccountRestAPI {
	
	private BankAccountService bankAccountService;
	
	public BankAccountRestAPI(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}
	
	@GetMapping("/accounts/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
		return bankAccountService.getBankAccount(accountId);
		
	}
	
	@GetMapping("/accounts")
	public List<BankAccountDTO> listAccounts(){
		return bankAccountService.bankAccountList();
	}
	
	@GetMapping("/accounts/{accountId}/operations")
	public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
		return bankAccountService.accountHistory(accountId);
	}
}
