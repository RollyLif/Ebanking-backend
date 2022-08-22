package com.lifungula.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CURR")
@Data @NoArgsConstructor @AllArgsConstructor
public class CurrentAccount extends BankAccount{
	private double overDraft;

}
