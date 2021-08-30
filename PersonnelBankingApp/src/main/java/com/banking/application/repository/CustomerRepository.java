package com.banking.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.banking.application.model.Customer;
import com.banking.application.utility.CustomerProjection;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{
	Customer findByUserName(String userName);
	
	Customer findByCustomerId(long customerId);
	
	CustomerProjection findCustomerByUserName(String userName);
	
	@Query("select c.panCardNo, c.aadhaarCardNo ,  c.emailId,c.firstName,c.lastName from Customer c where  c.panCardNo=:panNo or c.aadhaarCardNo=:aadhaarCardNo"
			+ " or c.emailId =:emailId")
	Optional<String> validateCustomer(String panNo,String aadhaarCardNo,String emailId);
}
