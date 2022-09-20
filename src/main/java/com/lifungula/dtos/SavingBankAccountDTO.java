package com.lifungula.dtos;

import java.util.Date;

import javax.persistence.Id;

import com.lifungula.enums.AccountStatus;

import lombok.Data;

@Data 
public abstract class SavingBankAccountDTO {
	@Id
	private String id;
	private double balance;
	private Date createdAt;
	private AccountStatus status;
	private CustomerDTO customerDTO;
	private double interestRate;
	

}
