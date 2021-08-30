package com.banking.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.banking.application.model.Beneficiary;
import com.banking.application.model.Customer;
import com.banking.application.request.BeneficiaryDTO;

@Mapper(componentModel = "spring")
public interface BeneficiaryMapper 
{
	@Mapping(target = "beneficiaryId", ignore = true)
	@Mapping(target = "beneficiaryAccountNo", expression = "java(Long.valueOf(beneficiaryDto.getBeneficiaryAccountNo()))")
	@Mapping(target = "transferLimit", expression = "java(Double.valueOf(beneficiaryDto.getTransferLimit()))")
	
	//@Mapping(target = "transactionType", expression = "java(com.training.pbpay.constant.AppConstant.DEBIT)")
	//@Mapping(target = "availableBalance", expression = "java(sourceAccount.getBalance()-transactionRequestDto.getTransactionAmount())")
	Beneficiary map(BeneficiaryDTO beneficiaryDto,Customer customer);
	
}
