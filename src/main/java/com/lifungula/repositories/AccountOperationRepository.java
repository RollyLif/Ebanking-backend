package com.lifungula.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifungula.entities.AccountOperation;
import com.lifungula.entities.Customer;

public interface AccountOperationRepository  extends JpaRepository<AccountOperation, Long>{

}
