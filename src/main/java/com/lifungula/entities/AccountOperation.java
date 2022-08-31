package com.lifungula.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.lifungula.enums.OperationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date operationDate;
	private double amount;
	private OperationType type;
	@ManyToOne
	private BankAccount bankAccount;
	private String description;

}
