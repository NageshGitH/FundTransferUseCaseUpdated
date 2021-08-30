package com.banking.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.banking.application.model.Account;
import com.banking.application.model.Beneficiary;
import com.banking.application.model.Transaction;
import com.banking.application.request.FundTransferDto;

@Mapper(componentModel="spring")
public interface FundTransferMapper
{
	public static final String DEBIT = "DEBIT";
	
	@Mapping(target = "transactionTime", expression = "java( new java.sql.Timestamp(new java.util.Date().getTime()))")
	@Mapping(target = "transactionType", expression = "java(DEBIT)")
	@Mapping(target = "fromAccount", expression = "java(account.getAccountNo())")
	@Mapping(target = "toAccount", expression = "java(benefiAcct.getBeneficiaryAccountNo())")
	@Mapping(target = "amount", expression = "java(Double.valueOf(fundTransDto.getTransferAmount()))")
	@Mapping(target = "account", source = "account")

	Transaction mapToTransaction(FundTransferDto fundTransDto, Account account,Beneficiary benefiAcct);
	@Mapping(target = "transactionDetails", ignore = true)
	@Mapping(target = "availableBalance", expression = "java(account.getAvailableBalance()-Double.valueOf(fundTransDto.getTransferAmount()))")
	Account mapToAccount(FundTransferDto fundTransDto,Account account);
}
