package com.banking.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.application.model.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>{

	Beneficiary findByBeneficiaryAccountNoAndCustomerCustomerId(long accountNo,long customerId);
	
	Optional<Beneficiary> findByBeneficiaryAccountNoAndCustomerUserName(long accountNo,String userName);
	
}
