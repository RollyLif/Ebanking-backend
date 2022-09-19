package com.lifungula.web;

import org.springframework.web.bind.annotation.RestController;

import com.lifungula.services.BankAccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
	
	private BankAccountService bankAccountService;

}
