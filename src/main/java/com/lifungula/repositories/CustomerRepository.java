package com.lifungula.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifungula.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	List<Customer> findByNameContains(String keyword);
}