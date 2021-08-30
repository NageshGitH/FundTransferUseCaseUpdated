package com.banking.application.mapper;

import com.banking.application.model.Account;
import com.banking.application.model.Beneficiary;
import com.banking.application.model.Transaction;
import com.banking.application.request.FundTransferDto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-30T10:20:30+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.3.1300.v20210419-1022, environment: Java 15 (Oracle Corporation)"
)
@Component
public class FundTransferMapperImpl implements FundTransferMapper {

    @Override
    public Transaction mapToTransaction(FundTransferDto fundTransDto, Account account, Beneficiary benefiAcct) {
        if ( fundTransDto == null && account == null && benefiAcct == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        if ( fundTransDto != null ) {
            transaction.setRemarks( fundTransDto.getRemarks() );
        }
        if ( account != null ) {
            transaction.setAccount( account );
        }
        transaction.setTransactionTime( new java.sql.Timestamp(new java.util.Date().getTime()) );
        transaction.setTransactionType( DEBIT );
        transaction.setFromAccount( account.getAccountNo() );
        transaction.setToAccount( benefiAcct.getBeneficiaryAccountNo() );
        transaction.setAmount( Double.valueOf(fundTransDto.getTransferAmount()) );

        return transaction;
    }

    @Override
    public Account mapToAccount(FundTransferDto fundTransDto, Account account) {
        if ( fundTransDto == null && account == null ) {
            return null;
        }

        Account account1 = new Account();

        if ( account != null ) {
            account1.setAccountId( account.getAccountId() );
            account1.setAccountNo( account.getAccountNo() );
            account1.setAccountType( account.getAccountType() );
            account1.setBankName( account.getBankName() );
            account1.setBranchName( account.getBranchName() );
            account1.setCreationDate( account.getCreationDate() );
            account1.setCustomer( account.getCustomer() );
            account1.setIfsCode( account.getIfsCode() );
        }
        account1.setAvailableBalance( account.getAvailableBalance()-Double.valueOf(fundTransDto.getTransferAmount()) );

        return account1;
    }
}
