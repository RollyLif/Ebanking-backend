package com.lifungula.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.lifungula.dtos.CustomerDTO;
import com.lifungula.entities.Customer;

@Service
public class BankAccountMapperImpl {

	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();	
		BeanUtils.copyProperties(customer, customerDTO);
//		customerDTO.setId(customer.getId());
//		customerDTO.setEmail(customer.getEmail());
//		customerDTO.setName(customer.getName());
		return customerDTO;
	}
	
	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}
}
