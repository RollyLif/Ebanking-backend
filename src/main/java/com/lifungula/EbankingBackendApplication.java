package com.lifungula;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.lifungula.dtos.BankAccountDTO;
import com.lifungula.dtos.CurrentBankAccountDTO;
import com.lifungula.dtos.CustomerDTO;
import com.lifungula.dtos.SavingBankAccountDTO;
import com.lifungula.exception.BalanceNotSufficientException;
import com.lifungula.exception.BankAccountNotFoundException;
import com.lifungula.exception.CustomerNotFoundException;
import com.lifungula.services.BankAccountService;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
			return args -> {
				Stream.of("Jojo", "Will", "Jophiel").forEach(name->{
					 CustomerDTO customer = new CustomerDTO();
					 customer.setName(name);
					 customer.setEmail(name+"@gmail.com");
					 bankAccountService.saveCustomer(customer);
				});
				bankAccountService.ListCustomers().forEach(customer -> {
					
					try {
						bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
						bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());
						List<BankAccountDTO> bankaccounts = bankAccountService.bankAccountList();
						
						for(BankAccountDTO bankAccount : bankaccounts) {
							for(int i=0; i<10; i++) {
								String accountId ="";
								if(bankAccount instanceof SavingBankAccountDTO) {
									accountId = ((SavingBankAccountDTO) bankAccount).getId();
									bankAccountService.credit(accountId, 10000+Math.random()*120000, "Credit");
									bankAccountService.debit(accountId, 1000+Math.random()*9000, "debit");
								}else {
									accountId = ((CurrentBankAccountDTO) bankAccount).getId();
									bankAccountService.credit(accountId, 10000+Math.random()*120000, "Credit");
									bankAccountService.debit(accountId, 1000+Math.random()*9000, "debit");
								}
							}
							
						}
					} catch (CustomerNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BankAccountNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BalanceNotSufficientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				});
			};
		}
	}

