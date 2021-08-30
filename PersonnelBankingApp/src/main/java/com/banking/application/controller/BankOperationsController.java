package com.banking.application.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.application.exception.UserNameAlreadyExistsException;
import com.banking.application.model.CustomerCredentials;
import com.banking.application.request.AccountOpeningRq;
import com.banking.application.request.BeneficiaryDTO;
import com.banking.application.request.FundTransferDto;
import com.banking.application.response.AccountCreationAcknowledgemnt;
import com.banking.application.service.BankOperationsService;
import com.banking.application.utility.CustomerProjection;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value ="/bankOperations")
@EnableTransactionManagement
@Slf4j
public class BankOperationsController
{
	private static final Logger logger = LoggerFactory.getLogger(BankOperationsController.class);
	
	@Autowired
	BankOperationsService bankOpertionService;
	
    @PostMapping("/")
	public ResponseEntity<AccountCreationAcknowledgemnt> customerAccountOpening(@Valid @RequestBody AccountOpeningRq request)  throws UserNameAlreadyExistsException
	{
    	logger.info("inside customerAccountOpening");
		return bankOpertionService.saveCustomerDetails(request);
	}
    
    @PostMapping("/{userName}")
	public ResponseEntity<String> saveBeneficiaryDetails(@Valid @RequestBody BeneficiaryDTO request,@PathVariable("userName") String userName)  
	{
    	logger.info("inside saveBeneficiaryDetails");
		return bankOpertionService.saveBeneficiary(request,userName);
	}
    
    @PostMapping("/checkCredentials")
	public ResponseEntity<String> checkCredentials(@Valid @RequestBody CustomerCredentials custCredentials)  
	{
    	logger.info("inside checkCredentials");
		return bankOpertionService.checkLoginCredential(custCredentials);
	}
    @PostMapping("/fundTransfer/{userName}")
   	public ResponseEntity<String> fundTransfer(@Valid @RequestBody FundTransferDto fundTransferDto,@PathVariable("userName") String userName)  
   	{
       	logger.info("inside checkCredentials");
   		return bankOpertionService.fundTransfer(fundTransferDto,userName);
   	}
    @GetMapping("/{userName}")
   	public ResponseEntity<CustomerProjection> getCustomerDetails(@PathVariable("userName") String userName)  
   	{
       	logger.info("inside checkCredentials");
   		return bankOpertionService.getCustomerDetails(userName);
   	}
    
}
