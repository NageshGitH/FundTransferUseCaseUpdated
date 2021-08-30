package com.banking.application.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.banking.application.model.Account;
import com.banking.application.model.Address;
import com.banking.application.model.Customer;
import com.banking.application.model.CustomerCredentials;
import com.banking.application.request.AccountOpeningRq;

@Mapper(componentModel = "spring")
public interface AccountOpeningMapper
{
	@Mapping(target = "addressId", ignore = true)
	Address mapToAddress(AccountOpeningRq request);
	
	@Mapping(target = "customerId", ignore = true)
	@Mapping(target = "dateOfBirth", expression = "java(java.time.LocalDate.parse(request.getDateOfBirth()))")
	@Mapping(target = "mobileNo", expression = "java(Long.valueOf(request.getMobileNo()))")
	@Mapping(target = "creationDate", expression = "java(new java.util.Date())")
	@Mapping(target = "accountDetails", ignore = true)
	@Mapping(target = "beneficiaryDetails", ignore = true)
	@Mapping(target="address",source="address")
	Customer mapToCustomer(AccountOpeningRq request, Address address);
	
	
	@Mapping(target = "accountId", ignore = true)
	@Mapping(target="customer",source="customer")
	@Mapping(target = "transactionDetails", ignore = true)
	@Mapping(target = "availableBalance", expression = "java(Double.valueOf(request.getOpeningDeposit()))")
	@Mapping(target = "accountNo", expression = "java(numbGen())")
	Account mapToAccount(AccountOpeningRq request,Customer customer);
	
	
	
	@Mapping(target = "custCredentialsId", ignore = true)
	@Mapping(target = "userName", expression = "java(customer.getUserName())")
	@Mapping(target = "userPassword", expression = "java(passwordGenerator())")
	@Mapping(target="customer",source="customer")
	CustomerCredentials mapToCred(AccountOpeningRq request,Customer customer);
	
	default long numbGen() 
	 {
		  while (true)
		  {
		        long numb = (long)(Math.random() * 100000000 * 100000000); // had to use this as int's are to small for a 12 digit number.
		        if (String.valueOf(numb).length() == 12)
		           return numb;
		  }
	  }
	
	default String passwordGenerator()
	{
		String password = UUID.randomUUID().toString().split("-")[1];
		return password;
	}

}
