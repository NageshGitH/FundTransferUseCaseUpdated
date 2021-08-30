package com.banking.application.mapper;

import com.banking.application.model.Account;
import com.banking.application.model.Address;
import com.banking.application.model.Customer;
import com.banking.application.model.CustomerCredentials;
import com.banking.application.request.AccountOpeningRq;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-30T10:20:30+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.3.1300.v20210419-1022, environment: Java 15 (Oracle Corporation)"
)
@Component
public class AccountOpeningMapperImpl implements AccountOpeningMapper {

    @Override
    public Address mapToAddress(AccountOpeningRq request) {
        if ( request == null ) {
            return null;
        }

        Address address = new Address();

        address.setAddress1( request.getAddress1() );
        address.setAddress2( request.getAddress2() );
        address.setCity( request.getCity() );
        if ( request.getPin() != null ) {
            address.setPin( Long.parseLong( request.getPin() ) );
        }
        address.setState( request.getState() );

        return address;
    }

    @Override
    public Customer mapToCustomer(AccountOpeningRq request, Address address) {
        if ( request == null && address == null ) {
            return null;
        }

        Customer customer = new Customer();

        if ( request != null ) {
            customer.setAadhaarCardNo( request.getAadhaarCardNo() );
            customer.setEmailId( request.getEmailId() );
            customer.setFirstName( request.getFirstName() );
            customer.setGender( request.getGender() );
            customer.setLastName( request.getLastName() );
            customer.setPanCardNo( request.getPanCardNo() );
            customer.setUserName( request.getUserName() );
        }
        if ( address != null ) {
            customer.setAddress( address );
        }
        customer.setDateOfBirth( java.time.LocalDate.parse(request.getDateOfBirth()) );
        customer.setMobileNo( Long.valueOf(request.getMobileNo()) );
        customer.setCreationDate( new java.util.Date() );

        return customer;
    }

    @Override
    public Account mapToAccount(AccountOpeningRq request, Customer customer) {
        if ( request == null && customer == null ) {
            return null;
        }

        Account account = new Account();

        if ( request != null ) {
            account.setAccountType( request.getAccountType() );
            account.setBankName( request.getBankName() );
            account.setBranchName( request.getBranchName() );
            account.setIfsCode( request.getIfsCode() );
        }
        if ( customer != null ) {
            account.setCustomer( customer );
            account.setCreationDate( customer.getCreationDate() );
        }
        account.setAvailableBalance( Double.valueOf(request.getOpeningDeposit()) );
        account.setAccountNo( numbGen() );

        return account;
    }

    @Override
    public CustomerCredentials mapToCred(AccountOpeningRq request, Customer customer) {
        if ( request == null && customer == null ) {
            return null;
        }

        CustomerCredentials customerCredentials = new CustomerCredentials();

        if ( customer != null ) {
            customerCredentials.setCustomer( customer );
        }
        customerCredentials.setUserName( customer.getUserName() );
        customerCredentials.setUserPassword( passwordGenerator() );

        return customerCredentials;
    }
}
