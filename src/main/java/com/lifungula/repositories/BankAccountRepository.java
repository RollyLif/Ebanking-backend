package com.lifungula.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifungula.entities.BankAccount;

public interface BankAccountRepository  extends JpaRepository<BankAccount, String>{

}
