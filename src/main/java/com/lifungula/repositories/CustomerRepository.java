package com.lifungula.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifungula.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
