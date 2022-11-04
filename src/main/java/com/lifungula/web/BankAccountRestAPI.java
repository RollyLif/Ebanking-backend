package com.lifungula.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifungula.dtos.AccountHistoryDTO;
import com.lifungula.dtos.AccountOperationDTO;
import com.lifungula.dtos.BankAccountDTO;
import com.lifungula.dtos.CreditDTO;
import com.lifungula.dtos.DebitDTO;
import com.lifungula.dtos.TransferRequestDTO;
import com.lifungula.exception.BalanceNotSufficientException;
import com.lifungula.exception.BankAccountNotFoundException;
import com.lifungula.services.BankAccountService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    @Autowired
	private BankAccountService bankAccountService;
	
	
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
	
	@GetMapping("/accounts/{accountId}/pageOperation")
	public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
			@RequestParam(name="page", defaultValue="0") int page,
			@RequestParam(name="size", defaultValue="5") int size) throws BankAccountNotFoundException{
		return bankAccountService.getAccountHistory(accountId, page, size);
	}
	
	@PostMapping("/accounts/debit")
	public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
	    System.out.println(debitDTO.getAccountId()+"  "+debitDTO.getAmount()+" "+debitDTO.getDescription());
	    this.bankAccountService.debit(debitDTO);
	    return debitDTO;
	}
	
	@PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }
	
	@PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountSource(), transferRequestDTO.getAccountDestination(), transferRequestDTO.getAmount());
   }
}
