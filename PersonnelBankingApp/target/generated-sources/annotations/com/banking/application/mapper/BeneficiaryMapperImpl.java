package com.banking.application.mapper;

import com.banking.application.model.Beneficiary;
import com.banking.application.model.Customer;
import com.banking.application.request.BeneficiaryDTO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-30T10:20:30+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.3.1300.v20210419-1022, environment: Java 15 (Oracle Corporation)"
)
@Component
public class BeneficiaryMapperImpl implements BeneficiaryMapper {

    @Override
    public Beneficiary map(BeneficiaryDTO beneficiaryDto, Customer customer) {
        if ( beneficiaryDto == null && customer == null ) {
            return null;
        }

        Beneficiary beneficiary = new Beneficiary();

        if ( beneficiaryDto != null ) {
            beneficiary.setBeneficiaryName( beneficiaryDto.getBeneficiaryName() );
            beneficiary.setIfsCode( beneficiaryDto.getIfsCode() );
        }
        if ( customer != null ) {
            beneficiary.setCustomer( customer );
        }
        beneficiary.setBeneficiaryAccountNo( Long.valueOf(beneficiaryDto.getBeneficiaryAccountNo()) );
        beneficiary.setTransferLimit( Double.valueOf(beneficiaryDto.getTransferLimit()) );

        return beneficiary;
    }
}
