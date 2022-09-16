package com.lifungula.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.lifungula.dtos.CustomerDTO;
import com.lifungula.exception.CustomerNotFoundException;
import com.lifungula.services.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
	private BankAccountService bankAccountService;
	
	@GetMapping("/customers")
	public List<CustomerDTO> customers(){
		return bankAccountService.ListCustomers();
	}
	
	@GetMapping("/customer/{id}")
	public CustomerDTO getCustomer(@PathVariable(name= "id") Long customerId) throws CustomerNotFoundException {
		return bankAccountService.getCustomer(customerId);
	}

}
