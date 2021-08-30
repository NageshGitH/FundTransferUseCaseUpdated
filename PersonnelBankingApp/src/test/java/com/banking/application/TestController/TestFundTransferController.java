package com.banking.application.TestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banking.application.controller.BankOperationsController;
import com.banking.application.exception.InvalidCredentialsException;
import com.banking.application.exception.ResourceNotFoundException;
import com.banking.application.exception.UserNameAlreadyExistsException;
import com.banking.application.model.CustomerCredentials;
import com.banking.application.request.AccountOpeningRq;
import com.banking.application.request.BeneficiaryDTO;
import com.banking.application.response.AccountCreationAcknowledgemnt;
import com.banking.application.service.BankOperationsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFundTransferController 
{

	@Mock
	BankOperationsService bankOperationsService;
	
	@InjectMocks
	BankOperationsController bankOperationsController;
	
	
	static CustomerCredentials custCredentials;
	static AccountOpeningRq accountOpeningDto;
	static BeneficiaryDTO dto;
	
	@BeforeAll
	public void setUp()
	{
		custCredentials =new CustomerCredentials();
		custCredentials.setUserName("Nagesh");
		custCredentials.setUserPassword("sri");
		
		accountOpeningDto = new AccountOpeningRq();
		accountOpeningDto.setUserName("AI0327");
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
		accountOpeningDto.setOpeningDeposit("4500");
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
		
	}
	
	
	@Test
	@DisplayName("Login Function: Positive Scenario")
	@Order(1)
	public void testAuthenticateUser()
	{
		//context
		when(bankOperationsService.checkLoginCredential(custCredentials)).thenReturn(new ResponseEntity<>("Login success", HttpStatus.OK));
		
		//Event
		ResponseEntity<?> result = bankOperationsController.checkCredentials(custCredentials);
		
		//out come
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	@Test
	@DisplayName("Login Function: Negative Scenario")
	@Order(2)
	public void testAuthenticateUser1()
	{
		//context
		when(bankOperationsService.checkLoginCredential(custCredentials)).thenThrow(InvalidCredentialsException.class);
		
		//Event and out come
		assertThrows(InvalidCredentialsException.class,()->bankOperationsController.checkCredentials(custCredentials));
	}
	
	

	@Test
	@DisplayName("Account Opening :: Postive Scenario")
	@Order(3)
	public void testCreateAccountOpeningPostive() {
		// Event
		when(bankOperationsService.saveCustomerDetails(accountOpeningDto)).thenReturn(new ResponseEntity<>(new AccountCreationAcknowledgemnt("Sucess"), HttpStatus.OK));
		//Event
		ResponseEntity<?> result = bankOperationsController.customerAccountOpening(accountOpeningDto);
	     //out come
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	
	@Test
	@DisplayName("Account Opening :: Negative Scenario")
	@Order(4)
	public void testCreateAccountOpeningNegative() {
		// context
		when(bankOperationsService.saveCustomerDetails(accountOpeningDto)).thenThrow(UserNameAlreadyExistsException.class);
		// Event and outcome
		assertThrows(UserNameAlreadyExistsException.class,()-> bankOperationsController.customerAccountOpening(accountOpeningDto));
	}
	
	
	@Test
	@DisplayName("Save Beneficiary   :: Postive Scenario")
	@Order(5)
	public void testSaveBenificaryPositive()
	{
		//context
		Mockito.when(bankOperationsService.saveBeneficiary(dto, "AI0326")).thenReturn(new ResponseEntity<>("Successfully added Beneficiary", HttpStatus.OK));
		//Event and  out come
		assertEquals(HttpStatus.OK, bankOperationsController.saveBeneficiaryDetails(dto, "AI0326").getStatusCode());
	}
	@Test
	@DisplayName("Save Beneficiary   :: Negative Scenario")
	@Order(6)
	public void testSaveBenificaryNegative() throws ResourceNotFoundException {
		Mockito.when(bankOperationsService.saveBeneficiary(dto, "AI0326")).thenThrow(ResourceNotFoundException.class);
		//Event and  out come
		assertThrows(ResourceNotFoundException.class, ()->bankOperationsService.saveBeneficiary(dto, "AI0326"));
	}

	
}

