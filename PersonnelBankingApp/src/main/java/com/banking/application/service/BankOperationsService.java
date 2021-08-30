package com.banking.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.banking.application.exception.BankOperationsException;
import com.banking.application.exception.BeneficiaryAccountNoAlreadyExistsException;
import com.banking.application.exception.InSufficientFundException;
import com.banking.application.exception.InvalidCredentialsException;
import com.banking.application.exception.ResourceNotFoundException;
import com.banking.application.exception.TransactionFailedException;
import com.banking.application.exception.TransferLimitException;
import com.banking.application.exception.UserNameAlreadyExistsException;
import com.banking.application.mapper.AccountOpeningMapper;
import com.banking.application.mapper.BeneficiaryMapper;
import com.banking.application.mapper.FundTransferMapper;
import com.banking.application.model.Account;
import com.banking.application.model.Address;
import com.banking.application.model.Beneficiary;
import com.banking.application.model.Customer;
import com.banking.application.model.CustomerCredentials;
import com.banking.application.repository.AccountRepository;
import com.banking.application.repository.AddressRepository;
import com.banking.application.repository.BeneficiaryRepository;
import com.banking.application.repository.CustomerCredentialsRepository;
import com.banking.application.repository.CustomerRepository;
import com.banking.application.repository.TransactionRepository;
import com.banking.application.request.AccountOpeningRq;
import com.banking.application.request.BeneficiaryDTO;
import com.banking.application.request.FundTransferDto;
import com.banking.application.response.AccountCreationAcknowledgemnt;
import com.banking.application.utility.CustomerProjection;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankOperationsService {

	private static final Logger logger = LoggerFactory.getLogger(BankOperationsService.class);
	@Autowired
	CustomerRepository custRepo;
	@Autowired
	AddressRepository addrRepo;
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	CustomerCredentialsRepository custCredRepo;
	@Autowired
	BeneficiaryRepository beneficiaryRepo;
	@Autowired
	TransactionRepository transRepo;
	
	@Autowired
	BeneficiaryMapper beneficiaryMapper;
	
	@Autowired
	AccountOpeningMapper accountOpeningMapper;
	
	@Autowired
	FundTransferMapper fundTransferMapper;


	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	public ResponseEntity<AccountCreationAcknowledgemnt> saveCustomerDetails(AccountOpeningRq request)
	 {
		logger.info("inside saveCustomerDetails  method");
		
		logger.info("Checking for if User Name exists.");
		if (Optional.ofNullable(custRepo.findByUserName(request.getUserName())).isPresent())
			throw new UserNameAlreadyExistsException("User Name already exists.Please try with another user name");
		
		logger.info("Validation customer pancard no,aadhaar No and email id");
		Optional<String> result = custRepo.validateCustomer(request.getPanCardNo(),request.getAadhaarCardNo(),
				request.getEmailId());
		
		if(result.isPresent())
		{
			StringBuffer strMsg = new StringBuffer();
			Arrays.asList(result.get().split(",")).stream().forEach(prop->
			{
				    System.out.println("prop::"+prop);
				    if(prop.equalsIgnoreCase(request.getPanCardNo()))				
						strMsg.append("Pan Card No ::  "+prop+",");
					else if(prop.equalsIgnoreCase(request.getAadhaarCardNo()))				
						strMsg.append("Aadhaar Card No ::  "+prop+" , ");
					else if(prop.equalsIgnoreCase(request.getEmailId()))				
						strMsg.append("Email Id ::  "+prop+" ");
									
			});
			if(strMsg.length() > 0)
				throw new BankOperationsException(strMsg.toString()+" registered with another customer.Please try with another!!!!");
		}
		
		Address  addr =addrRepo.save(accountOpeningMapper.mapToAddress(request));
		logger.info("saved address entity");
		
		Customer cust = custRepo.save(accountOpeningMapper.mapToCustomer(request, addr));
		logger.info("saved  customer entity");
		
		accountRepo.save(accountOpeningMapper.mapToAccount(request, cust));
		logger.info("saved account entity");
		
		CustomerCredentials credentials=custCredRepo.save(accountOpeningMapper.mapToCred(request, cust));
		logger.info("saved customer credentials entity");
		
		StringBuffer strMsg = new StringBuffer();
		strMsg.append("Account Opened for Customer :: ").append(cust.getFirstName()).append(" " + cust.getLastName());
		strMsg.append("");
		strMsg.append("  Use credentials for Login: ").append("User Name:: " + credentials.getUserName())
				.append(" Password:: " + credentials.getUserPassword());
		
		return new ResponseEntity<>(new AccountCreationAcknowledgemnt(strMsg.toString()), HttpStatus.OK);
	}

	public ResponseEntity<String> saveBeneficiary(BeneficiaryDTO dto, String userName) 
	{
		logger.info("inside saveBeneficiary  method");
		
		logger.info("Checking for if customer exists.");
		
		Customer customer = Optional.ofNullable(custRepo.findByUserName(userName))
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "Customer Name", userName));
		
		checkBeneficiaryAccount(Long.valueOf(dto.getBeneficiaryAccountNo()),userName,"create");
		
		beneficiaryRepo.save(beneficiaryMapper.map(dto, customer));
		
		return new ResponseEntity<>("Successfully added Beneficiary", HttpStatus.OK);
	}

	public ResponseEntity<String> checkLoginCredential(CustomerCredentials credentials) {
		logger.info("inside checkLoginCredential method");
		logger.info("Checking for if customer credentials");
		
		if (Optional.ofNullable(
				custCredRepo.findByUserNameAndUserPassword(credentials.getUserName(), credentials.getUserPassword()))
				.isEmpty()) {
			throw new InvalidCredentialsException("Authetication Failed! Please provide valid User Name or Password ");
		}
		return new ResponseEntity<>("Authetication Success", HttpStatus.OK);
	}
	
	@Transactional(rollbackFor = TransactionFailedException.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<String> fundTransfer(FundTransferDto fundTransDto, String userName)
	{
		// Check is the customer exists, if does not exists return error message
				// else continue 
		Customer customer = Optional.ofNullable(custRepo.findByUserName(userName))
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "Customer Name", userName));
		// Check is the account number exists, if does not exists return error message
		// else continue .
		Account fromAccDetails = Optional
				.ofNullable(accountRepo.findByAccountNoAndCustomerCustomerId(
						Long.valueOf(fundTransDto.getFromAccountNo()), customer.getCustomerId()))
				.orElseThrow(() -> new ResourceNotFoundException("Account Details", "Account Number",
						fundTransDto.getFromAccountNo()));
		Beneficiary toAccDetails = Optional.ofNullable(beneficiaryRepo.findByBeneficiaryAccountNoAndCustomerCustomerId(
			 							Long.valueOf(fundTransDto.getToAccountNo()), customer.getCustomerId())).orElseThrow(() -> new
			 										ResourceNotFoundException("Customers Beneficiary Account Details  ", "Account Number", fundTransDto.getToAccountNo()));
		isFundTranferAllowed(Double.valueOf(fundTransDto.getTransferAmount()), fromAccDetails.getAvailableBalance(),fromAccDetails);
		checkBeneficaryTranferLimit(Double.valueOf(fundTransDto.getTransferAmount()),toAccDetails.getTransferLimit(),toAccDetails);
		transRepo.save(fundTransferMapper.mapToTransaction(fundTransDto, fromAccDetails, toAccDetails));
		accountRepo.save(fundTransferMapper.mapToAccount(fundTransDto,fromAccDetails));
		return new ResponseEntity<>("Transaction Done Successfully ", HttpStatus.OK);
	}

	public ResponseEntity<CustomerProjection> getCustomerDetails(String userName) {
		logger.info("inside getCustomerDetails method");
		return new ResponseEntity<>(custRepo.findCustomerByUserName(userName), HttpStatus.OK);
	}
	
	public boolean isFundTranferAllowed(double transferAmt,double avaialableAmount,Account fromAccDetails) throws InSufficientFundException
	{
		if (transferAmt > avaialableAmount) {
			throw new InSufficientFundException(
					"InSufficent balance for the account number::" + fromAccDetails.getAccountNo());
		}else
		{
			  return true;
		}
	}
	
	public Beneficiary checkBeneficiaryAccount(Long beneficiaryAccNo,String  userName,String requestType)
	{
		StringBuffer strMsg = new StringBuffer();
		
		if(requestType.equalsIgnoreCase("create"))
			strMsg.append("Beneficiary Account Number ").append(beneficiaryAccNo).append(" already assocated with Customer");
		else
			strMsg.append("Beneficiary Account Number ").append(beneficiaryAccNo).append(" is not assocated with Customer");
			
		Optional<Beneficiary> beneficary = beneficiaryRepo.findByBeneficiaryAccountNoAndCustomerUserName(beneficiaryAccNo, userName);
		
		logger.info("beneficary:::"+beneficary.isEmpty()+" "+beneficary.isPresent());
		   
		   if(beneficary.isEmpty())
			   return new Beneficiary();
		   else 
			    throw new BeneficiaryAccountNoAlreadyExistsException(strMsg.toString());
	}
	
	public boolean checkBeneficaryTranferLimit(double transferAmt,double avaialableAmount,Beneficiary toAccDetails) 
	{
		if (transferAmt > avaialableAmount) 
		{
			throw new TransferLimitException("Transfer Limit Excessed for this benificary account number::"
					+ toAccDetails.getBeneficiaryAccountNo()+" Maximum tranfer Limit: "+toAccDetails.getTransferLimit());
		}else
		{
			return true;
		}
	}
}
