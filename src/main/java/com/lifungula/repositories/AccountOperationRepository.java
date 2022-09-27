package com.lifungula.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lifungula.entities.AccountOperation;

public interface AccountOperationRepository  extends JpaRepository<AccountOperation, Long>{
	List<AccountOperation> findByBankAccountId(String id);
	Page<AccountOperation> findByBankAccountId(String accountId, Pageable pageable);
}
