package com.banking.application.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="Beneficiary")
public class Beneficiary 
{
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name="beneficiary_id")
	 private Long beneficiaryId;
	 
	 @Column(name="beneficiary_name")
	 private String beneficiaryName;
	 
	 @Column(name="beneficiary_account_no")
	 private long beneficiaryAccountNo;
	 
	 @Column(name="transfer_limit")
	 private double transferLimit;
	 
	 @Column(name="ifs_code")
	 private String ifsCode;
	 
	 @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	 @JoinColumn(name="customer_id")
	 private Customer customer;
}
