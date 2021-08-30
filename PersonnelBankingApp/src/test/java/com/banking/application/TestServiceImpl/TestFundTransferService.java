package com.banking.application.TestServiceImpl;

import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banking.application.exception.InvalidCredentialsException;
import com.banking.application.exception.ResourceNotFoundException;
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
import com.banking.application.service.BankOperationsService;
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFundTransferService {

	@Captor
	private ArgumentCaptor<CustomerCredentials> postArgumentCaptor;

	@InjectMocks
	BankOperationsService bankOperationsService;

	@Mock
	CustomerCredentialsRepository custCredRepo;

	@Mock
	CustomerRepository custRepo;

	@Mock
	AccountRepository accRepo;

	@Mock
	BeneficiaryRepository beneficiaryRepo;

	@Mock
	TransactionRepository transRepo;

	@Mock
	AddressRepository addrRepo;
	
	
	@Mock
	BeneficiaryMapper beneficiaryMapper;
	
	@Mock
	AccountOpeningMapper accountOpeningMapper;
	
	@Mock
	FundTransferMapper fundTransferMapper;
	

	static CustomerCredentials custCredentials,custCredentials1;
	static Customer customer;
	static Account account;
	static Address addr;
	static AccountOpeningRq accountOpeningDto;
	static FundTransferDto fundTranferDto;
	static BeneficiaryDTO dto;
	static Beneficiary benificary;

	@BeforeAll
	public static void setUp() {
		// mock Customer Credentials details as in database
		custCredentials = new CustomerCredentials();
		custCredentials.setUserName("Nagesh");
		custCredentials.setUserPassword("sri");

		custCredentials1 = new CustomerCredentials();
		custCredentials1.setUserName("Nagesh");
		custCredentials1.setUserPassword("sri123");
		
		// mock customer details as in database
		customer = new Customer();
		customer.setCustomerId(1l);
		customer.setUserName("AI0326");

		// mock account details as in database
		account = new Account();
		account.setAvailableBalance(5500);
		account.setAccountNo(123456l);

		// mock benificary details as in database
		benificary = new Beneficiary();
		//benificary.setBeneficiaryAccountNo(4567);
		benificary.setCustomer(customer);
		benificary.setTransferLimit(6000);

		// set values for accountopening dto
		accountOpeningDto = new AccountOpeningRq();
		accountOpeningDto.setUserName("AI0329");
		accountOpeningDto.setFirstName("Devi");
		accountOpeningDto.setLastName("Konatham");
		accountOpeningDto.setDateOfBirth("2018-01-18");
		accountOpeningDto.setGender("male");
		accountOpeningDto.setMobileNo("7680092889");
		accountOpeningDto.setEmailId("skonatham@gmail.com");
		accountOpeningDto.setPanCardNo("AULPK1507H");
		accountOpeningDto.setAadhaarCardNo("400607192799");
		accountOpeningDto.setAddress1("Adress1");
		accountOpeningDto.setAddress2("Address2");
		accountOpeningDto.setCity("KP");
		accountOpeningDto.setState("Hyd");
		accountOpeningDto.setPin("500072");
		accountOpeningDto.setOpeningDeposit("9500");
		accountOpeningDto.setBankName("HDFC");
		accountOpeningDto.setBranchName("Kukatpally");
		accountOpeningDto.setIfsCode("HDFC00314");
		accountOpeningDto.setAccountType("Checking");

		// set values for BeneficiaryDTO
		dto = new BeneficiaryDTO();
		dto.setBeneficiaryAccountNo("4567");
		dto.setBeneficiaryName("Nagesh");
		dto.setIfsCode("HDF00314");
		dto.setTransferLimit("8000");

		// set values for fund transfer dto
		fundTranferDto = new FundTransferDto();
		fundTranferDto.setFromAccountNo("123456");
		fundTranferDto.setToAccountNo("4567");
		fundTranferDto.setRemarks("Expenses");
		fundTranferDto.setTransferAmount("1000");
		
		addr = new Address();
		addr.setAddressId(1l);
		addr.setAddress1("KP");
		addr.setAddress2("Hyd");
		addr.setState("Telagana");
		addr.setCity("Hyd");
		addr.setPin(500072);
	}

	@Test
	@DisplayName("Account Opening :: Postive Scenario")
	@Order(1)
	public void testCreateAccountOpeningPostive() {
		// Event
		
		Mockito.when(accountOpeningMapper.mapToAddress(accountOpeningDto)).thenReturn(addr);
		
		Mockito.when(addrRepo.save(Mockito.any(Address.class))).thenAnswer(i -> {
			Address address = i.getArgument(0);
			address.setAddressId(1l);
			return address;
		});
		
		Mockito.when(accountOpeningMapper.mapToCustomer(accountOpeningDto,addr)).thenReturn(customer);
		
		Mockito.when(custRepo.save(Mockito.any(Customer.class))).thenAnswer(i -> {
			Customer cust = i.getArgument(0);
			cust.setCustomerId(1l);
			return cust;
		});
		
		Mockito.when(accountOpeningMapper.mapToAccount(accountOpeningDto,customer)).thenReturn(account);
		
		Mockito.when(accRepo.save(Mockito.any(Account.class))).thenAnswer(i -> {
			Account acct = i.getArgument(0);
			acct.setAccountId(0l);
			return acct;
		});
		
       Mockito.when(accountOpeningMapper.mapToCred(accountOpeningDto,customer)).thenReturn(custCredentials);
		
		Mockito.when(custCredRepo.save(Mockito.any(CustomerCredentials.class))).thenAnswer(i -> {
			CustomerCredentials custCredentials = i.getArgument(0);
			custCredentials.setCustCredentialsId(1l);
			return custCredentials;
		});
		
	  ResponseEntity<?> result = bankOperationsService.saveCustomerDetails(accountOpeningDto);
	
	  assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	
	@Test
	@DisplayName("Account Opening :: Negative Scenario")
	@Order(2)
	public void testCreateAccountOpeningNegative() {
		// context
		Mockito.when(custRepo.findByUserName("AI0329")).thenThrow(UserNameAlreadyExistsException.class);
		// Event and outcome
		assertThrows(UserNameAlreadyExistsException.class,()-> bankOperationsService.saveCustomerDetails(accountOpeningDto));
	}

	@Test
	@DisplayName("Check Customer Login Credentials :: Postive Scenario")
	@Order(3)
	public void testAuthenticateUserPostive() 
	{
		Mockito.when(custCredRepo.findByUserNameAndUserPassword("Nagesh", "sri")).thenReturn(custCredentials);
		// Event
		ResponseEntity<?> result = bankOperationsService.checkLoginCredential(custCredentials);
		// out come
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	
	@Test
	@DisplayName("Check Customer Login Credentials:: Negative Scenrio")
	@Order(4)
	public void testAuthenticateUserNegative()  {
		// context
		Mockito.when(custCredRepo.findByUserNameAndUserPassword("Nagesh", "sri")).thenReturn(null);
		// Event and outcome
		assertThrows(InvalidCredentialsException.class, ()->bankOperationsService.checkLoginCredential(custCredentials));
	}

	@Test
	@DisplayName("Save Beneficiary   :: Postive Scenario")
	@Order(5)
	public void testSaveBenificaryPositive()
	{
		//context
		Mockito.when(custRepo.findByUserName(customer.getUserName())).thenReturn(customer);
		//Event and  out come
		assertEquals(HttpStatus.OK, bankOperationsService.saveBeneficiary(dto, "AI0326").getStatusCode());
	}
	@Test
	@DisplayName("Save Beneficiary   :: Negative Scenario")
	@Order(6)
	public void testSaveBenificaryNegative() throws ResourceNotFoundException {
		Mockito.when(custRepo.findByUserName("AI0326")).thenReturn(null);
		//Event and  out come
		assertThrows(ResourceNotFoundException.class, ()->bankOperationsService.saveBeneficiary(dto, "AI0326"));
	}

	@Test
	@DisplayName("Test FundTransfer Transaction :: Postive Scenario")
	@Order(7)
	public void testFundTransfer() 
	{
		// context
		Mockito.when(custRepo.findByUserName("AI0326")).thenReturn(customer);		
		Mockito.when(accRepo.findByAccountNoAndCustomerCustomerId(Long.valueOf(123456), 1l)).thenReturn(account);
		Mockito.when(beneficiaryRepo.findByBeneficiaryAccountNoAndCustomerCustomerId(
				Long.valueOf(4567), 1l)).thenReturn(benificary);
		
        assertEquals(bankOperationsService.checkBeneficaryTranferLimit(Double.valueOf(fundTranferDto.getTransferAmount()), benificary.getTransferLimit(),benificary),true);
		 
        assertEquals(bankOperationsService.isFundTranferAllowed(Double.valueOf(fundTranferDto.getTransferAmount()), account.getAvailableBalance(),account),true);
		 
		//Event and  out come
		assertEquals(HttpStatus.OK, bankOperationsService.fundTransfer(fundTranferDto, "AI0326").getStatusCode());
	}
	
	@Test
	@DisplayName("Test FundTransfer Transaction :: Negative Scenario")
	@Order(8)
	public void testFundTransferNegative() 
	{
		// context
		 assumeFalse(bankOperationsService.checkBeneficaryTranferLimit(Double.valueOf(fundTranferDto.getTransferAmount()), benificary.getTransferLimit(),benificary));
		 
		 assumeFalse(bankOperationsService.isFundTranferAllowed(Double.valueOf(fundTranferDto.getTransferAmount()), account.getAvailableBalance(),account));
		 
		assertThrows(ResourceNotFoundException.class, ()->bankOperationsService.fundTransfer(fundTranferDto, "AI0326"));
		
	}
	
	

}
